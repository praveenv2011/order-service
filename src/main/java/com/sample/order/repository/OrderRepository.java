package com.sample.order.repository;

import com.sample.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByUserIdAndOrderId(Long userId, Long orderId);

    List<Order> findAllByUserId(Long userId);
}
