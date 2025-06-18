package com.estudo.rafsburguer.service;

import com.estudo.rafsburguer.dto.input.order.CreateOrderDto;
import com.estudo.rafsburguer.dto.input.order.CreateOrderItemDto;
import com.estudo.rafsburguer.dto.input.order.UpdateStatusOrderDto;
import com.estudo.rafsburguer.dto.output.order.RecoveryOrderDto;
import com.estudo.rafsburguer.entities.Delivery;
import com.estudo.rafsburguer.entities.Order;
import com.estudo.rafsburguer.entities.OrderItem;
import com.estudo.rafsburguer.entities.ProductVariation;
import com.estudo.rafsburguer.enums.PaymentMethod;
import com.estudo.rafsburguer.enums.Status;
import com.estudo.rafsburguer.exceptions.OrderNotFoundException;
import com.estudo.rafsburguer.exceptions.ProductVariationNotFoundException;
import com.estudo.rafsburguer.mappers.OrderMapper;
import com.estudo.rafsburguer.repositories.OrderRepository;
import com.estudo.rafsburguer.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;


    public RecoveryOrderDto createOrder(CreateOrderDto createOrderDto){
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;

        for (CreateOrderItemDto createOrderItemDto : createOrderDto.orderItems()){

            ProductVariation productVariation = productVariationRepository
                    .findProductIdAndProductVariationId(createOrderItemDto.productId(), createOrderItemDto.productVariationId())
                    .orElseThrow(ProductVariationNotFoundException::new);

            amount = amount.add(productVariation.getPrice().multiply(new BigDecimal(createOrderItemDto.quantity())));

            OrderItem orderItem = OrderItem.builder()
                    .quantity(createOrderItemDto.quantity())
                    .productVariation(productVariation)
                    .build();

            orderItems.add(orderItem);
        }

        Delivery delivery = Delivery.builder()
                .receiverName(createOrderDto.createDeliveryDto().receiverName())
                .address(createOrderDto.createDeliveryDto().address())
                .houseNumber(createOrderDto.createDeliveryDto().houseNumber())
                .complement(createOrderDto.createDeliveryDto().complement())
                .distric(createOrderDto.createDeliveryDto().district())
                .zipCode(createOrderDto.createDeliveryDto().zipCode())
                .city(createOrderDto.createDeliveryDto().city())
                .state(createOrderDto.createDeliveryDto().state())
                .phoneNumber(createOrderDto.createDeliveryDto().phone_number())
                .build();

        Order order = Order.builder()
                .paymentMethod(PaymentMethod.valueOf(createOrderDto.paymentMethod().toUpperCase()))
                .orderItems(orderItems)
                .amount(amount)
                .delivery(delivery)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        delivery.setOrder(order);

        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public RecoveryOrderDto getOrderById(Long orderId){

        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new));
    }

    public Page<RecoveryOrderDto> getOrders(Pageable pageable){

        Page<Order> orderPage = orderRepository.findAll(pageable);

        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }

    public Page<RecoveryOrderDto> getOrderByStatus(String statusName, Pageable pageable){

        Page<Order> orderPage = orderRepository.findByStatus(Status.valueOf(statusName.toUpperCase()), pageable);

        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }

    public RecoveryOrderDto changeOrderStatus(Long orderId, UpdateStatusOrderDto updateStatusOrderDto){

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setStatus(Status.valueOf(updateStatusOrderDto.status().toUpperCase()));
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public void deleteOrderById(Long orderId){

        if (!orderRepository.existsById(orderId)){
            throw new OrderNotFoundException();
        }

        orderRepository.deleteById(orderId);
    }

}
