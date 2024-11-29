package com.shops.dreamshops.repository;

import com.shops.dreamshops.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem ,Long> {
}
