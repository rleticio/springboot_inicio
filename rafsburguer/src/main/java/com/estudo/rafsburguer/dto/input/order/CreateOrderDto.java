package com.estudo.rafsburguer.dto.input.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDto(
        @NotBlank(message = "{not.blank.message}")
        String paymentMethod,
        @NotEmpty(message = "{not.empty.message}")
        List<CreateOrderItemDto> orderItems,
        @NotNull(message = "{not.null.message}")
        @Valid
        CreateDeliveryDto createDeliveryDto
) {
}
