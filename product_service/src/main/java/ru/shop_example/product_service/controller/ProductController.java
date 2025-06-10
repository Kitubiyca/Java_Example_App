package ru.shop_example.product_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.shop_example.product_service.dto.*;
import ru.shop_example.product_service.mapper.PageMapper;
import ru.shop_example.product_service.mapper.ProductMapper;
import ru.shop_example.product_service.mapper.ProductSearchParamsMapper;
import ru.shop_example.product_service.service.ProductService;
import ru.shop_example.product_service.validation.groups.OnCreate;
import ru.shop_example.product_service.validation.groups.OnUpdate;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final PageMapper pageMapper;
    private final ProductSearchParamsMapper productSearchParamsMapper;

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
    @GetMapping("{id}")
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
                            responseCode = "201",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IdDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
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
    @PatchMapping
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
    @DeleteMapping ("{id}")
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
    @GetMapping
    public PageDto<ProductDto> getAllProducts(Pageable pageable){
        log.info("Called getProductById controller method");
        Page<ProductDto> page = productService.getAllProducts(pageable).map(productMapper::productToProductDto);
        return pageMapper.PageToPageDto(page);
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
    @GetMapping("byType/{typeId}")
    public PageDto<ProductDto> getProductsByTypeId(@PathVariable UUID typeId, Pageable pageable){
        log.info("Called getProductById controller method");
        Page<ProductDto> page = productService.getAllProductsByTypeId(typeId, pageable).map(productMapper::productToProductDto);
        return pageMapper.PageToPageDto(page);
    }

    @Operation(
            summary = "Получение списка продуктов по заданным критериям",
            description = "Возвращает DTO с со всеми найденными продуктами",
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
    @GetMapping("search")
    public PageDto<ProductDto> searchProducts(@RequestBody @Validated ProductSearchParamsDto productSearchParamsDto, Pageable pageable){
        log.info("Called searchProducts controller method");
        Page<ProductDto> page = productService.searchProducts(productSearchParamsMapper.productSearchParamsDtoToProductSearchParams(productSearchParamsDto), pageable).map(productMapper::productToProductDto);
        return pageMapper.PageToPageDto(page);
    }

    @Operation(
            summary = "Проверка на существование продкута",
            description = "Возвращает DTO, говорящее о существовании записе",
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
    @GetMapping("{id}/exists")
    public BooleanDto isProductExist(@PathVariable UUID id){
        log.info("Called isExistProduct controller method");
        return productService.isProductExist(id);
    }
}
