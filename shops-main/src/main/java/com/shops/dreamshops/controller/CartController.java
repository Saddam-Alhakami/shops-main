package com.shops.dreamshops.controller;

import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart/{id}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("Success", cart));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable  Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("totalPrice/{id}")
    public ResponseEntity<ApiResponse> getTotalPrice(Long id) {
        try {
            BigDecimal totalPrice=cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total Price is ",totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NOT FOUND",null));

        }

    }

}
