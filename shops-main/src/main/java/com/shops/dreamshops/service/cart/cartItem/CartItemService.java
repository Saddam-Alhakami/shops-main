package com.shops.dreamshops.service.cart.cartItem;

import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.CartItem;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.repository.CartItemRepository;
import com.shops.dreamshops.repository.CartRepository;
import com.shops.dreamshops.service.cart.CartService;
import com.shops.dreamshops.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {

        // Haal het Cart object op
        Cart cart = cartService.getCart(cartId);

        // Haal het Product object op
        Product product = productService.getProductById(productId);

        // Zoek naar een bestaand CartItem voor dit product
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId) // Vergelijk met ==
                .findFirst()
                .orElse(null);  // Gebruik null in plaats van een nieuw CartItem aan te maken

        // Als het item nog niet in de winkelwagen zit, maak een nieuw CartItem aan
        if (cartItem == null) {
            cartItem = new CartItem();  // Nu maak je een nieuw CartItem aan
            cartItem.setCart(cart);     // Stel de relatie in
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            // Als het item al bestaat, update de hoeveelheid
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // Stel de totaalprijs in op basis van hoeveelheid en prijs
        cartItem.setTotalPrice();

        // Voeg het item toe aan de winkelwagen (indien nieuw), of update het bestaande
        cart.addItem(cartItem);

        // Sla zowel het CartItem als de Cart op
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }


    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        // Verwijder het item uit de cart
        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);

        // Herbereken de positie van elk item in de cart
        int index = 1;
        for (CartItem item : cart.getItems()) {
            item.setPosition(index++);
            cartItemRepository.save(item);
        }

        // Sla de bijgewerkte cart op
        cartRepository.save(cart);
    }



    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

        Cart cart = cartService.getCart(cartId);                  // Haal de cart op
        Product product = productService.getProductById(productId); // Haal het product op

        // Zoek het bestaande CartItem en werk het bij als het aanwezig is
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId)) // Gebruik equals() voor objectvergelijking
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);                        // Stel de nieuwe hoeveelheid in
                    item.setUnitPrice(item.getProduct().getPrice());    // Stel de eenheidsprijs in
                    item.setTotalPrice();                              // Bereken de totale prijs opnieuw
                });

        // Bereken en stel het nieuwe totaalbedrag van de cart in
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)                      // Haal de totale prijs van elk item op
                .reduce(BigDecimal.ZERO, BigDecimal::add);         // Voeg alle prijzen samen

        cart.setTotalAmount(totalAmount);                          // Stel het totaalbedrag in
        cartRepository.save(cart);                                 // Sla de bijgewerkte cart op
    }
    @Override
    public CartItem getItemFromCart(Long cartId, Long productId){
        Cart cart=cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("notfound"));


    }


}
