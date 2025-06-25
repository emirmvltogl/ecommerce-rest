package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private Long id;
    private String username;
    private LocalDate orderDate;
    private double totalAmount;
    private List<OrderItemDTO> items;
    public Long getId() {
      return id;
    }
    public void setId(Long id) {
      this.id = id;
    }
    public String getUsername() {
      return username;
    }
    public void setUsername(String username) {
      this.username = username;
    }
    public LocalDate getOrderDate() {
      return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
      this.orderDate = orderDate;
    }
    public double getTotalAmount() {
      return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
      this.totalAmount = totalAmount;
    }
    public List<OrderItemDTO> getItems() {
      return items;
    }
    public void setItems(List<OrderItemDTO> items) {
      this.items = items;
    }

    // Getter ve Setter'lar

    

  }