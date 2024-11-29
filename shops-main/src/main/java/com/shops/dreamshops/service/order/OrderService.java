package com.shops.dreamshops.service.order;

import com.shops.dreamshops.dto.OrderDto;
import com.shops.dreamshops.enums.OrderStatus;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Cart;
import com.shops.dreamshops.model.Order;
import com.shops.dreamshops.model.OrderItem;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.repository.OrderRepository;
import com.shops.dreamshops.repository.ProductRepository;
import com.shops.dreamshops.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItem(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart) {

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;

    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()


                    );

                }).toList();

    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDto).orElseThrow(
                () -> new ResourceNotFoundException("NOT FOUND"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::convertToDto).toList();

    }
    private OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}