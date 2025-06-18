package com.estudo.rafsburguer.dto.output.order;

public record RecoveryDeliveryDto(

        Long id,
        String receiverName,
        String address,
        String houseNumber,
        String complement,
        String district,
        String zipCode,
        String city,
        String state,
        String phoneNumber
) {
}
