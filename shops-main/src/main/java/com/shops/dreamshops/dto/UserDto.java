package com.shops.dreamshops.dto;

import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String Password;
    private CartDto cart;
    private List<OrderDto> orders;
}
