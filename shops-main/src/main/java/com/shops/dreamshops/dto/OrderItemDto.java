package com.shops.dreamshops.dto;

import com.shops.dreamshops.model.Order;
import com.shops.dreamshops.model.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
