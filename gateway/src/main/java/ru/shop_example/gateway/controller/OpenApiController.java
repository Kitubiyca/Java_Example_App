package ru.shop_example.gateway.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.dto.OpenApiServiceDto;
import ru.shop_example.gateway.repository.ServiceDataReactiveRepository;

@RestController
@Hidden
@RequiredArgsConstructor
public class OpenApiController {

    private final ServiceDataReactiveRepository serviceDataReactiveRepository;

    @GetMapping("/service-api-endpoints")
    public Mono<OpenApiServiceDto> getServiceApiEndpoints() {
        return serviceDataReactiveRepository.findAll()
                .map(sd -> new OpenApiServiceDto.Url(
                        sd.getName(),
                        String.format("/%s/v3/api-docs", sd.getName())
                ))
                .collectList()
                .map(urls -> {
                    OpenApiServiceDto dto = new OpenApiServiceDto();
                    dto.getUrls().addAll(urls);
                    return dto;
                });
    }

}
