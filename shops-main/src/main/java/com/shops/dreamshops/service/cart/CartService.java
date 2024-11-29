package com.shops.dreamshops.service.cart;

import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.User;
import com.shops.dreamshops.repository.CartItemRepository;
import com.shops.dreamshops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService  {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator=new AtomicLong(0);

    @Override
    public Cart getCart(Long cartId) {
     Cart cart=   cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart Not Found"));
    BigDecimal totalAmount= cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);

     return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
       cartItemRepository.deleteAllByCartId(id);
       cart.getItems().clear();
       cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id); // Haal de Cart op
        return cart.getTotalAmount();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        Cart cart=  cartRepository.findByUserId(userId);
        return cart;
    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart =new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                        });


    }





}
