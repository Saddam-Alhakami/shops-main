package com.shops.dreamshops.service.order;

import com.shops.dreamshops.dto.OrderDto;
import com.shops.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
