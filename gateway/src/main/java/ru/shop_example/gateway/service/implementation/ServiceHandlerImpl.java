package ru.shop_example.gateway.service.implementation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.entity.ServiceData;
import ru.shop_example.gateway.repository.ServiceDataReactiveRepository;
import ru.shop_example.gateway.service.OpenApiDocRetriever;
import ru.shop_example.gateway.service.ServiceHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceHandlerImpl implements ServiceHandler {

    private final RedisRouteDefinitionRepository redisRouteDefinitionRepository;
    private final ServiceDataReactiveRepository serviceDataReactiveRepository;
    private final OpenApiDocRetriever openApiDocRetriever;

    private static final String ROUTE_DEFINITION_ID_PATTERN = "%s_%s_%s";
    private static final String ROUTE_DEFINITION_URI_PATTERN = "lb://%s";

    private final Map<String, Supplier<FilterDefinition>> definedFilters = Map.of(
            "AUTHENTICATED", () -> new FilterDefinition("Authentication")
    );

    @Override
    public Flux<Void> handleServices(Flux<String> services) {
        log.info("Handling services");
        return services.flatMap(this::handleService);
    }

    private Mono<Void> handleService(String serviceName) {
        log.debug("Handling service: {}", serviceName);
        return openApiDocRetriever.getServiceApiDocByName(serviceName)
                .flatMap(openAPI -> checkServiceOpenApiDoc(serviceName, openAPI))
                .doOnError(e -> log.error(e.getMessage()));
    }

    private Mono<Void> checkServiceOpenApiDoc(String serviceName, OpenAPI openAPI) {
        int hash = openAPI.hashCode();
        return serviceDataReactiveRepository.find(serviceName)
                .map(serviceData -> serviceData.getHash().equals(hash))
                .defaultIfEmpty(false)
                .flatMap(isUpToDate -> isUpToDate ?
                        Mono.fromRunnable(() -> log.info("Service {} is up to date.", serviceName)) :
                        handleServiceOpenApiDoc(serviceName, openAPI, hash));
    }

    private Mono<Void> handleServiceOpenApiDoc(String serviceName, OpenAPI openAPI, int hash) {
        List<RouteDefinition> routeDefinitions = createDefinitions(serviceName, openAPI);
        routeDefinitions.add(generateSwaggerUIRouteDefinition(serviceName));

        Mono<Void> deleteOldRoutes = redisRouteDefinitionRepository.getRouteDefinitions()
                .map(RouteDefinition::getId)
                .filter(id -> id.contains(serviceName))
                .map(Mono::just)
                .flatMap(id -> redisRouteDefinitionRepository
                        .delete(id)
                        .onErrorResume(e -> {
                            log.error("Failed to delete route {}", id);
                            return Mono.empty();
                        }))
                .ignoreElements();

        Mono<Void> saveNewRoutes = Flux.fromIterable(routeDefinitions)
                .map(Mono::just)
                .flatMap(redisRouteDefinitionRepository::save)
                .ignoreElements();

        Mono<Void> saveServiceData = serviceDataReactiveRepository
                        .save(new ServiceData(serviceName, hash, routeDefinitions.size()))
                .then();

        return deleteOldRoutes
                .then(saveNewRoutes)
                .then(saveServiceData)
                .doOnSuccess(o -> log.info("Service {} data updated.", serviceName));
    }

    private List<RouteDefinition> createDefinitions(String serviceName, OpenAPI openAPI) {
        return openAPI.getPaths().entrySet().stream()
                .map(item -> generateRouteDefinitionsFromPathItem(serviceName, item))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<RouteDefinition> generateRouteDefinitionsFromPathItem(String serviceName, Map.Entry<String, PathItem> pathItem) {
        String uri = pathItem.getKey();
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        for (Map.Entry<PathItem.HttpMethod, Operation> operationEntry : pathItem.getValue().readOperationsMap().entrySet()) {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId(String.format(ROUTE_DEFINITION_ID_PATTERN, serviceName, uri, operationEntry.getKey().toString()));
            routeDefinition.setUri(URI.create(String.format(ROUTE_DEFINITION_URI_PATTERN, serviceName)));
            routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Path=%s", uri)));
            routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Method=%s", operationEntry.getKey().toString())));
            routeDefinition.getFilters().addAll(createFiltersFromTags(operationEntry.getValue().getTags()));
            routeDefinitions.add(routeDefinition);
        }
        routeDefinitions.add(generateOptionsRouteDefinition(serviceName, uri));
        return routeDefinitions;
    }

    private List<FilterDefinition> createFiltersFromTags(List<String> tags) {
        List<FilterDefinition> filters = tags.stream()
                .filter(definedFilters::containsKey)
                .map(tag -> definedFilters.get(tag).get())
                .collect(Collectors.toList());
        List<String> roles = tags.stream()
                .filter(tag -> tag.startsWith("ROLE_"))
                .map(tag -> tag.substring(5))
                .toList();
        if (!roles.isEmpty()) {
            FilterDefinition rolesFilter = new FilterDefinition("Role");
            rolesFilter.addArg("roles", String.join(",", roles));
            filters.add(rolesFilter);
        }
        return filters;
    }

    private RouteDefinition generateSwaggerUIRouteDefinition(String serviceName) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(String.format(ROUTE_DEFINITION_ID_PATTERN, serviceName, "/v3/api-docs", PathItem.HttpMethod.GET));
        routeDefinition.setUri(URI.create(String.format(ROUTE_DEFINITION_URI_PATTERN, serviceName)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Path=/%s/v3/api-docs", serviceName)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Method=%s", PathItem.HttpMethod.GET)));
        routeDefinition.getFilters().add(new FilterDefinition(String.format("RewritePath=/%s/v3/api-docs, /v3/api-docs", serviceName)));
        return routeDefinition;
    }

    private RouteDefinition generateOptionsRouteDefinition(String serviceName, String uri) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(String.format(ROUTE_DEFINITION_ID_PATTERN, serviceName, uri, PathItem.HttpMethod.OPTIONS));
        routeDefinition.setUri(URI.create(String.format(ROUTE_DEFINITION_URI_PATTERN, serviceName)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Path=%s", uri)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Method=%s", PathItem.HttpMethod.OPTIONS)));
        return routeDefinition;
    }
}
