package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{
  
}
