package com.shops.dreamshops.controller;

import com.shops.dreamshops.dto.UserDto;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.CartItem;
import com.shops.dreamshops.model.User;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.cart.CartService;
import com.shops.dreamshops.service.cart.cartItem.CartItemService;
import com.shops.dreamshops.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cart/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            // Haal de geauthenticeerde gebruiker op
            User user = userService.getAuthenticatedUser();
            System.out.println(user);

            // Haal een bestaande cart op of initialiseer een nieuwe
            Cart cart = cartService.initializeNewCart(user);

            // Voeg het product toe aan de cart
            cartItemService.addItemToCart(cart.getId(), productId, quantity);

            // Retourneer een succesvolle response
            return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", null));

        } catch (ResourceNotFoundException e) {
            // Fout als een resource (bijv. gebruiker of product) niet wordt gevonden
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error: " + e.getMessage(), null));

        } catch (JwtException e) {
            // Fout bij authenticatie (bijv. JWT-token ongeldig)
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse("Authentication failed: " + e.getMessage(), null));

        } catch (IllegalArgumentException e) {
            // Fout bij ongeldige invoer (bijv. quantity <= 0)
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse("Invalid input: " + e.getMessage(), null));

        } catch (Exception e) {
            // Algemene foutafhandeling voor onvoorziene fouten
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", null));
        }
    }




    @DeleteMapping("/delete")
    public  ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId,
                                                           @RequestParam Long productId){
       try{ cartItemService.removeItemFromCart(cartId,productId);
        return ResponseEntity.ok(new ApiResponse("item  deleted ",null));
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    };


    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam Long cartId,
                                                          @RequestParam Long productId,
                                                          @RequestParam int quantity){
        try {
            cartItemService.updateItemQuantity(cartId,productId,quantity);
            return ResponseEntity.ok(new ApiResponse("updated",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not found",e.getMessage()));
        }


    }

    @GetMapping("/item-from-cart")
    public ResponseEntity<ApiResponse> getItemFromCart(@RequestParam Long cartId,@RequestParam Long productId){
        try {
            CartItem cartItem=cartItemService.getItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("item is ",cartItem));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NOT FOUND",e.getMessage()));
        }

    }




}
