package ru.shop_example.gateway.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shop_example.gateway.dto.OpenApiServiceDto;
import ru.shop_example.gateway.entity.ServiceData;
import ru.shop_example.gateway.repository.ServiceDataRepository;

@RestController
@Hidden
@RequiredArgsConstructor
public class OpenApiController {

    private final ServiceDataRepository serviceDataRepository;

    @GetMapping("/service-api-endpoints")
    public OpenApiServiceDto getServiceApiEndpoints() {
        Iterable<ServiceData> serviceData = serviceDataRepository.findAll();
        OpenApiServiceDto openApiServiceDto = new OpenApiServiceDto();
        for (ServiceData sd : serviceData) {
            openApiServiceDto.getUrls().add(new OpenApiServiceDto.Url(sd.getName(), String.format("/%s/v3/api-docs", sd.getName())));
        }
        return openApiServiceDto;
    }

}
