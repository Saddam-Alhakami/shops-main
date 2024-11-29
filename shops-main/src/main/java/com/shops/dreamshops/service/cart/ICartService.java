package com.shops.dreamshops.service.cart;

import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ICartService  {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart getCartByUserId(Long userId);

    Cart initializeNewCart(User user);
}
