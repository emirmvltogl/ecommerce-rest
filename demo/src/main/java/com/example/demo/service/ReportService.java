package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repo.OrderRepository;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Double getDailyRevenue(LocalDate date) {
        return orderRepository.getDailyRevenue(date);
    }

    public long getTodayOrderCount() {
        return orderRepository.countOrdersByDate(LocalDate.now());
    }

    public double getTotalRevenue() {
        return orderRepository.findAll()
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }
}
