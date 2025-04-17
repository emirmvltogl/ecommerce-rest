package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "stock")
  private int stock;

  @Column(name = "price")
  private int price;

  @Column(name = "url")
  private String url;

  public Product(String productName, int stock, int price, String url) {
    this.productName = productName;
    this.stock = stock;
    this.price = price;
    this.url = url;
  }

}
