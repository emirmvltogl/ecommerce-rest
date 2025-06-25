package com.example.demo.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Order;
import com.example.demo.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Bir kullanıcıya ait tüm siparişler
    List<Order> findByUser(User user);

    // Belirli bir güne ait siparişler
    List<Order> findByOrderDate(LocalDate date);

    // Belirli bir tarihten sonraki siparişler
    List<Order> findByOrderDateAfter(LocalDate date);

    // Belirli bir tarihteki toplam ciro
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate = :date")
    Double getDailyRevenue(LocalDate date);

    // Tüm zamanların toplam cirosu
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalRevenue();

    // Günlük sipariş sayısı
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = :date")
    Long countOrdersByDate(LocalDate date);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllOrderByDateDesc();

    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);



}
