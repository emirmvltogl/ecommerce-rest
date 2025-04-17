package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllData() {
    return new ResponseEntity<>(productService.getAllData(), HttpStatus.OK);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable int id) {
    return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
  }

  @PostMapping("/products")
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product theProduct) {
    return new ResponseEntity<>(productService.updateProduct(id, theProduct), HttpStatus.OK);
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable int id) {
    Product deletingProduct = productService.findProductById(id);
    if (deletingProduct == null) {
      return new ResponseEntity<>("product id is not found : " + id, HttpStatus.NOT_FOUND);
    }
    productService.deleteProduct(id);

    return new ResponseEntity<>("deleted produt : " + id, HttpStatus.OK);
  }

}
