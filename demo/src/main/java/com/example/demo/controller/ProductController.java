package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  // ─── PUBLIC ──────────────────────────────────────────────────

  @GetMapping
  public ResponseEntity<List<Product>> listAll() {
    return ResponseEntity.ok(productService.getAllData());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getOne(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findProductById(id));
  }

  // ─── MANAGER ──────────────────────────────────────────────────

  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product p) {
    return new ResponseEntity<>(productService.createProduct(p), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable Long id,
      @RequestBody Product p) {
    return ResponseEntity.ok(productService.updateProduct(id, p));
  }

  // ─── ADMIN ────────────────────────────────────────────────────

  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    if (productService.findProductById(id) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("product id not found: " + id);
    }
    productService.deleteProduct(id);
    return ResponseEntity.ok("deleted product: " + id);
  }
}
