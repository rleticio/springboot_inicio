package com.estudo.rafsburguer.dto.output.order;

import com.estudo.rafsburguer.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RecoveryOrderDto(
        Long id,
        String paymentMethod,
        Status status,
        BigDecimal amount,
        @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdDate,
        List<RecoveryOrderItemDto> orderItems,
        RecoveryDeliveryDto delivery
) {
}
