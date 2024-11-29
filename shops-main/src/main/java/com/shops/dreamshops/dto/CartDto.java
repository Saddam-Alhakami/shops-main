package com.shops.dreamshops.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {

    private Long id;
    private BigDecimal totalAmount=BigDecimal.ZERO;
    private Set<CartItemDto> items;

}
