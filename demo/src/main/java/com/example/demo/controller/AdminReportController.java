package com.example.demo.controller;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DailyRevenueDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.SoldProductInfoDTO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.ReportService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

  private final ReportService reportService;
  private final OrderService orderService;

  @Autowired
  public AdminReportController(ReportService reportService, OrderService orderService) {
    this.reportService = reportService;
    this.orderService = orderService;
  }

  // Günlük toplam gelir (bugünün)
  @GetMapping("/daily-revenue")
  public ResponseEntity<Double> getTodayRevenue() {
    return ResponseEntity.ok(reportService.getDailyRevenue(LocalDate.now()));
  }

  // Bugünkü sipariş sayısı
  @GetMapping("/today-order-count")
  public ResponseEntity<Long> getTodayOrderCount() {
    return ResponseEntity.ok(reportService.getTodayOrderCount());
  }

  // Tüm zamanların toplam kazancı
  @GetMapping("/total-revenue")
  public ResponseEntity<Double> getTotalRevenue() {
    return ResponseEntity.ok(reportService.getTotalRevenue());
  }

  // Satılan tüm ürünlerin listesi
  @GetMapping("/sold-products")
  public ResponseEntity<List<SoldProductInfoDTO>> getSoldProductsInfo() {
    return ResponseEntity.ok(orderService.getSoldProductInfoList());
  }

  // Tüm siparişler (admin paneli için)
  @GetMapping("/orders")
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrdersWithDetails());
  }

  @GetMapping("/orders-desc-dto")
  public ResponseEntity<List<OrderDTO>> getAllOrdersByDescDTO() {
    List<Order> orders = orderService.findOrdersByDesc();
    List<OrderDTO> dtos = orders.stream()
        .map(orderService::convertToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  // Tarih aralığına göre siparişler
  @GetMapping("/orders-by-date")
  public ResponseEntity<List<OrderDTO>> getOrdersByDateRange(
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate) {

    LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().minusDays(7);
    LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

    return ResponseEntity.ok(orderService.getOrdersByDateRange(start, end));
  }

  // En çok satılan ürünler
  @GetMapping("/top-selling-products")
  public ResponseEntity<List<SoldProductInfoDTO>> getTopSellingProducts(@RequestParam(defaultValue = "5") int topN) {
    return ResponseEntity.ok(orderService.getTopSellingProducts(topN));
  }

  // Günlük gelir grafiği için tarih aralıklı veriler
  @GetMapping("/daily-revenue-range")
  public ResponseEntity<List<DailyRevenueDTO>> getDailyRevenueRange(
      @RequestParam String startDate,
      @RequestParam String endDate) {

    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);
    return ResponseEntity.ok(orderService.getDailyRevenue(start, end));
  }

  @GetMapping("/export-orders-csv")
  public void exportOrdersCsv(HttpServletResponse response) throws IOException, java.io.IOException {
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=orders.csv");

    List<OrderDTO> orders = orderService.getAllOrdersWithDetails();

    try (PrintWriter writer = response.getWriter()) {
      writer.println("OrderId,Username,OrderDate,TotalAmount,ProductDetails");
      for (OrderDTO order : orders) {
        String products = order.getItems().stream()
            .map(item -> item.getProductName() + " x" + item.getQuantity())
            .collect(Collectors.joining("; "));
        writer.printf("%d,%s,%s,%.2f,%s\n", order.getId(), order.getUsername(),
            order.getOrderDate(), order.getTotalAmount(), products);
      }
    }
  }

}
