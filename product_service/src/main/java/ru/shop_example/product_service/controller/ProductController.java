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
import ru.shop_example.product_service.dto.ProductDto;
import ru.shop_example.product_service.mapper.ProductMapper;
import ru.shop_example.product_service.service.ProductService;
import ru.shop_example.product_service.validation.groups.OnCreate;
import ru.shop_example.product_service.validation.groups.OnUpdate;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("product/entity")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(
            summary = "Получение продукта по id",
            description = "Возвращает DTO с данными о продукте",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping("get/{id}")
    public ProductDto getProductById(@PathVariable UUID id){
        log.info("Called getProductById controller method");
        return productMapper.productToProductDto(productService.getProductById(id));
    }

    @Operation(
            summary = "Создание нового продукта",
            description = "Возвращает DTO с id продукта",
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
    @PostMapping ("create")
    public IdDto createProduct(@RequestBody @Validated(OnCreate.class) ProductDto productDto){
        log.info("Called createProduct controller method");
        return new IdDto(productService.createProduct(productMapper.productDtoToProduct(productDto)).getId());
    }

    @Operation(
            summary = "Обновление продукта",
            description = "Обновляет данные продукта",
            tags = {"AUTHENTICATED", "ROLE_MANAGER"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные обновлены"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping ("update")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProduct(@RequestBody @Validated(OnUpdate.class) ProductDto productDto){
        log.info("Called updateProduct controller method");
        productService.updateProduct(productMapper.productDtoToProduct(productDto));
    }

    @Operation(
            summary = "Удаление продукта",
            description = "Удаление продукта по id",
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
    public void deleteProductById(@PathVariable UUID id){
        log.info("Called deleteProductById controller method");
        productService.deleteProductById(id);
    }

    @Operation(
            summary = "Получение списка продуктов",
            description = "Возвращает DTO с со всеми продуктами",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping("getAll")
    public List<ProductDto> getAllProducts(){
        log.info("Called getProductById controller method");
        return productService.getAllProducts().stream().map(productMapper::productToProductDto).toList();
    }

    @Operation(
            summary = "Получение списка продуктов определённого типа",
            description = "Возвращает DTO с со всеми продуктами определённого типа",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping("getAllByType/{typeId}")
    public List<ProductDto> getProductsByTypeId(@PathVariable UUID typeId){
        log.info("Called getProductById controller method");
        return productService.getAllProductsByTypeId(typeId).stream().map(productMapper::productToProductDto).toList();
    }
}
