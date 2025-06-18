package com.estudo.rafsburguer.dto.output.product;

public record RecoveryDelivery(
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
