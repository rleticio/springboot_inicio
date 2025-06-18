package com.estudo.rafsburguer.mappers;

import com.estudo.rafsburguer.dto.output.order.RecoveryDeliveryDto;
import com.estudo.rafsburguer.dto.output.order.RecoveryOrderDto;
import com.estudo.rafsburguer.dto.output.order.RecoveryOrderItemDto;
import com.estudo.rafsburguer.entities.Delivery;
import com.estudo.rafsburguer.entities.Order;
import com.estudo.rafsburguer.entities.OrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // O target especifica o campo de destino no RecoveryOrderDto.
    // O  qualifiedByName especifica o método de mapeamento que deve ser usado para preencher esse campo.
    @Mapping(qualifiedByName = "mapDeliveryToRecoveryDeliveryDto", target = "delivery")
    @Mapping(qualifiedByName = "mapOrderItemListToRecoveryOrderItemDtoList", target = "orderItems")
    @Named("mapOrderToRecoveryOrderDto")
    RecoveryOrderDto mapOrderToRecoveryOrderDto(Order order);

    @IterableMapping(qualifiedByName = "mapOrderItemToRecoveryOrderItemDto")
    @Named("mapOrderItemListToRecoveryOrderItemDtoList")
    List<RecoveryOrderItemDto> mapOrderItemListToRecoveryOrderItemDtoList(List<OrderItem> orderItems);

    @Named("mapOrderItemToRecoveryOrderItemDto")
    RecoveryOrderItemDto mapOrderItemToRecoveryOrderItemDto(OrderItem orderItem);

    @Named("mapDeliveryToRecoveryDeliveryDto")
    RecoveryDeliveryDto mapDeliveryToRecoveryDeliveryDto(Delivery delivery);
}
