package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "`orders`")  
public class Order {
    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;

    private LocalDate orderDate;

    public Order() {
    }

    public Order(Long id, User user, List<OrderItem> items, double totalAmount, LocalDate orderDate) {
      this.id = id;
      this.user = user;
      this.items = items;
      this.totalAmount = totalAmount;
      this.orderDate = orderDate;
    }

    public Order(User user, List<OrderItem> items, double totalAmount, LocalDate orderDate) {
      this.user = user;
      this.items = items;
      this.totalAmount = totalAmount;
      this.orderDate = orderDate;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

    public List<OrderItem> getItems() {
      return items;
    }

    public void setItems(List<OrderItem> items) {
      this.items = items;
    }

    public double getTotalAmount() {
      return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
      this.totalAmount = totalAmount;
    }

    public LocalDate getOrderDate() {
      return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
      this.orderDate = orderDate;
    }

  

}

