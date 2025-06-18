package com.estudo.rafsburguer.dto.input.product;

public record UpdateProductDto(
        String name,
        String description,
        Boolean available
) {
}
