package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DailyRevenueDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.SoldProductInfoDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repo.OrderItemRepository;
import com.example.demo.repo.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        orderRepository.save(order);

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }
    }

    public List<OrderDTO> getAllOrdersWithDetails() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO) // convertToDTO burada tek tek Order nesnelerini dönüştürüyor
                .collect(Collectors.toList());
    }

    // convertToDTO tek bir Order'ı OrderDTO'ya dönüştürür
    public OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());

        // OrderItem listesi DTO'ya çevriliyor
        List<OrderItemDTO> items = order.getItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setProductName(item.getProduct().getProductName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(items);
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setUsername(order.getUser().getUsername());
        return dto;
    }

    // Tarihe göre filtreleme metodu
    public List<OrderDTO> getOrdersByDateRange(LocalDate start, LocalDate end) {
        return orderRepository.findByOrderDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SoldProductInfoDTO> getSoldProductInfoList() {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getItems().stream().map(item -> {
                    SoldProductInfoDTO dto = new SoldProductInfoDTO();
                    dto.setProductName(item.getProduct().getProductName());
                    dto.setQuantity(item.getQuantity());
                    dto.setOrderDate(order.getOrderDate());
                    dto.setUsername(order.getUser().getUsername());
                    dto.setPrice(item.getPrice());
                    return dto;
                }))
                .collect(Collectors.toList());
    }

    public List<SoldProductInfoDTO> getTopSellingProducts(int topN) {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getProductName(),
                        Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(topN)
                .map(e -> {
                    SoldProductInfoDTO dto = new SoldProductInfoDTO();
                    dto.setProductName(e.getKey());
                    dto.setQuantity(e.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<DailyRevenueDTO> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        Order::getOrderDate,
                        Collectors.summingDouble(Order::getTotalAmount)))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new DailyRevenueDTO(e.getKey().toString(), e.getValue()))
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersByDesc(){
        return orderRepository.findAllOrderByDateDesc();
    }


}
