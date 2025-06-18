package com.estudo.rafsburguer.controller;

import com.estudo.rafsburguer.dto.input.order.CreateOrderDto;
import com.estudo.rafsburguer.dto.output.order.RecoveryOrderDto;
import com.estudo.rafsburguer.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<RecoveryOrderDto> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto){
        return new ResponseEntity<>(orderService.createOrder(createOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<RecoveryOrderDto> getOrderById(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }

    @GetMapping("/status/{}")
    public ResponseEntity<Page<RecoveryOrderDto>> getOrderByStatus(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
            Pageable pageable,
            @PathVariable String statusName
    ){
        return new ResponseEntity<>(orderService.getOrderByStatus(statusName, pageable),HttpStatus.OK);
    }










}
