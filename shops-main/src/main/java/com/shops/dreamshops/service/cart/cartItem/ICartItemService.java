package com.shops.dreamshops.service.cart.cartItem;

import com.shops.dreamshops.model.CartItem;

public interface ICartItemService {
     void addItemToCart(Long cartId,Long productId,int quantity);
     void removeItemFromCart(Long cartId,Long productId);
     void updateItemQuantity(Long cartId,Long productId,int quantity);


     CartItem getItemFromCart(Long cartId, Long productId);
}
