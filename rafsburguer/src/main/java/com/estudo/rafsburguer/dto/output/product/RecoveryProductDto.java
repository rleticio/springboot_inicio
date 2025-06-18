package com.estudo.rafsburguer.dto.output.product;

import java.util.List;

public record RecoveryProductDto(
        Long id,
        String name,
        String description,
        String category,
        Boolean available,
        List<RecoveryProductVariationDto> productVariations
) {
}
