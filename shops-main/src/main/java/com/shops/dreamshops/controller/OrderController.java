package com.shops.dreamshops.controller;

import com.shops.dreamshops.dto.OrderDto;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Order;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
@RestController
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId){
        try {
            OrderDto order=orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Item order success",order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order=orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order",order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/get-users-order/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Order", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }




}
