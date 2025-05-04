package ru.shop_example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OpenApiServiceDto {

    private List<Url> urls = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    public static class Url{
        private String name;
        private String url;
    }
}
