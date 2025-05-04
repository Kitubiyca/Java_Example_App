package ru.shop_example.product_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.shop_example.product_service.dto.ErrorDto;
import ru.shop_example.product_service.dto.IdDto;
import ru.shop_example.product_service.dto.ProductTypeDto;
import ru.shop_example.product_service.mapper.ProductTypeMapper;
import ru.shop_example.product_service.service.ProductTypeService;
import ru.shop_example.product_service.validation.groups.OnCreate;
import ru.shop_example.product_service.validation.groups.OnUpdate;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("product/type")
public class ProductTypeController {

    private final ProductTypeService productTypeService;
    private final ProductTypeMapper productTypeMapper;

    @Operation(
            summary = "Получение типа продуктов по id",
            description = "Возвращает DTO типа продукта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductTypeDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping("get/{id}")
    public ProductTypeDto getProductTypeById(@PathVariable UUID id){
        log.info("Called getProductTypeById controller method");
        return productTypeMapper.productTypeToProductTypeDto(productTypeService.getProductTypeById(id));
    }

    @Operation(
            summary = "Создание нового типа продуктов",
            description = "Создаёт нвоый тип продуктов и возвращает его id",
            tags = {"AUTHENTICATED", "ROLE_MANAGER"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IdDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping("create")
    public IdDto createProductType(@RequestBody @Validated(OnCreate.class) ProductTypeDto productTypeDto){
        log.info("Called createProductType controller method");
        return new IdDto(productTypeService.createProductType(productTypeMapper.productTypeDtoToProductType(productTypeDto)).getId());
    }

    @Operation(
            summary = "Обновление продукта",
            description = "Обновляет продукт по id",
            tags = {"AUTHENTICATED", "ROLE_MANAGER"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные Обновлены"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping ("update")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProductType(@RequestBody @Validated(OnUpdate.class) ProductTypeDto productTypeDto){
        log.info("Called updateProductType controller method");
        productTypeService.updateProductType(productTypeMapper.productTypeDtoToProductType(productTypeDto));
    }

    @Operation(
            summary = "Удаление продукта",
            description = "Удаляет продукт по id",
            tags = {"AUTHENTICATED", "ROLE_MANAGER"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные удалены"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping ("delete/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteProductTypeById(@PathVariable UUID id){
        log.info("Called deleteProductTypeById controller method");
        productTypeService.deleteProductTypeById(id);
    }
}
