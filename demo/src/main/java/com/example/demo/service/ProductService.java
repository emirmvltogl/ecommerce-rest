package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;

@Service
public class ProductService {

  private ProductRepo productRepo;

  @Autowired
  public ProductService(ProductRepo productRepo) {
    this.productRepo = productRepo;
  }

  public List<Product> getAllData() {
    return productRepo.findAll();
  }

  public Product findProductById(int id) {
    Product tempProduct = productRepo.findById(id).get();
    return tempProduct;
  }

  public Product updateProduct(int id, Product product) {
    Product theProduct = productRepo.findById(id).get();
    theProduct.setProductName(product.getProductName());
    theProduct.setStock(product.getStock());
    theProduct.setPrice(product.getPrice());
    theProduct.setUrl(product.getUrl());
    productRepo.save(theProduct);
    return theProduct;
  }

  public void deleteProduct(int id) {
    productRepo.deleteById(id);
    System.out.println("deleted product id : " + id);
  }

  public Product createProduct(Product theProduct) {
    Product tempProduct = theProduct;
    productRepo.save(theProduct);
    return tempProduct;

  }
}
