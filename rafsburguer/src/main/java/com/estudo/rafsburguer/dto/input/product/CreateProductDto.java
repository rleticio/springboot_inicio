package com.estudo.rafsburguer.dto.input.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductDto(
        @NotBlank(message = "{not.blank.message}")
        String name,
        @NotBlank(message = "{not.blank.message}")
        String description,
        @NotBlank(message = "{not.blank.message}")
        String category,
        @NotNull(message = "{not.null.message}")
        Boolean available,

        List<@Valid CreateProductVariationDto> productVariations
) {
}
