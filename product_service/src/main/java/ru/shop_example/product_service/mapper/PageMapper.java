package ru.shop_example.product_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.shop_example.product_service.dto.PageDto;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <T> PageDto<T> PageToPageDto(Page<T> page){
        if (page == null) return null;
        PageDto<T> pageDto = new PageDto<>();
        pageDto.setContent(page.getContent());
        pageDto.setNumber(page.getNumber());
        pageDto.setSize(page.getSize());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setTotalPages(page.getTotalPages());
        return pageDto;
    }
}
