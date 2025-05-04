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
import ru.shop_example.gateway.repository.ServiceDataRepository;
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
    private final ServiceDataRepository serviceDataRepository;
    private final OpenApiDocRetriever openApiDocRetriever;

    private static final String ROUTE_DEFINITION_ID_PATTERN = "%s-%s-%s";
    private static final String ROUTE_DEFINITION_URI_PATTERN = "lb://%s";

    private final Map<String, Supplier<FilterDefinition>> definedFilters = Map.of(
            "AUTHENTICATED", () -> new FilterDefinition("Authentication")
    );

    @Override
    public void handleServices(Flux<String> services) {
        log.info("Handling services");
        services.flatMap(openApiDocRetriever::getServiceApiDocByName).subscribe(this::handleServiceOpenApiDoc);
    }

    private void handleServiceOpenApiDoc(OpenAPI openAPI) {
        log.info("Handling OpenAPI entity");
        String serviceName = openAPI.getInfo().getTitle();
        Integer serviceHash = openAPI.hashCode();
        if (isDocUpToDate(serviceName, serviceHash)) return;
        List<RouteDefinition> routeDefinitions = openAPI.getPaths().entrySet().stream()
                .map(item -> generateRouteDefinitionsFromPathItem(serviceName, item))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        routeDefinitions.add(generateSwaggerUIRouteDefinition(serviceName));
        serviceDataRepository.deleteByName(serviceName);
        redisRouteDefinitionRepository.getRouteDefinitions()
                .filter(routeDefinition -> routeDefinition.getId().contains(serviceName))
                .flatMap(routeDefinition -> redisRouteDefinitionRepository.delete(Mono.just(routeDefinition.getId())))
                .subscribe();
        Flux.fromIterable(routeDefinitions)
                .map(Mono::just)
                .flatMap(redisRouteDefinitionRepository::save)
                .subscribe();
        serviceDataRepository.save(new ServiceData(serviceName, serviceHash, routeDefinitions.size()));
    }

    private boolean isDocUpToDate(String openAPIName, Integer openAPIHash) {
        log.info("Checking OpenAPIDoc version for {}", openAPIName);
        return serviceDataRepository.findById(openAPIName).map(data -> data.getHash().equals(openAPIHash)).orElse(false);
    }

    private List<RouteDefinition> generateRouteDefinitionsFromPathItem(String serviceName, Map.Entry<String, PathItem> pathItem) {
        log.info("Creating new route definitions from path item for service {}", serviceName);
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
        return routeDefinitions;
    }

    private List<FilterDefinition> createFiltersFromTags(List<String> tags){
        log.info("Creating new filters from tags");
        List<FilterDefinition> filters = tags.stream()
                .filter(definedFilters::containsKey)
                .map(tag -> definedFilters.get(tag).get())
                .collect(Collectors.toList());
        List<String> roles = tags.stream()
                .filter(tag -> tag.startsWith("ROLE_"))
                .map(tag -> tag.substring(5))
                .toList();
        if (!roles.isEmpty()){
            FilterDefinition rolesFilter = new FilterDefinition("Role");
            rolesFilter.addArg("roles", String.join(",", roles));
            filters.add(rolesFilter);
        }
        return filters;
    }

    private RouteDefinition generateSwaggerUIRouteDefinition(String serviceName){
        log.info("Creating new swagger route for {}", serviceName);
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(String.format(ROUTE_DEFINITION_ID_PATTERN, serviceName, "/v3/api-docs", PathItem.HttpMethod.GET));
        routeDefinition.setUri(URI.create(String.format(ROUTE_DEFINITION_URI_PATTERN, serviceName)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Path=/%s/v3/api-docs", serviceName)));
        routeDefinition.getPredicates().add(new PredicateDefinition(String.format("Method=%s", PathItem.HttpMethod.GET)));
        routeDefinition.getFilters().add(new FilterDefinition(String.format("RewritePath=/%s/v3/api-docs, /v3/api-docs", serviceName)));
        return routeDefinition;
    }
}
