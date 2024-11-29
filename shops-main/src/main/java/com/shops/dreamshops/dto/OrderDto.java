package com.shops.dreamshops.dto;

import com.shops.dreamshops.enums.OrderStatus;
import com.shops.dreamshops.model.OrderItem;
import com.shops.dreamshops.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderDto {

    private Long orderId;
    private Long userId;
    private String orderDate;
    private BigDecimal totalAmount;
    private String orderStatus;
    private List<OrderItemDto> orderItem ;
}
