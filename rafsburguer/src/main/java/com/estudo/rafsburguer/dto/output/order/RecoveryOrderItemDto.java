package com.estudo.rafsburguer.dto.output.order;

import com.estudo.rafsburguer.dto.output.product.RecoveryProductVariationDto;

public record RecoveryOrderItemDto(

        Long id,
        RecoveryProductVariationDto productVariation,
        Integer quantity
) {
}
