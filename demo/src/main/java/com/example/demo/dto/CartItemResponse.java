package com.example.demo.dto;

public class CartItemResponse {
    private Long id;
    private String productName;
    private String url;
    private double price;
    private int quantity;

    public CartItemResponse(Long id, String productName, String url, double price, int quantity) {
        this.id = id;
        this.productName = productName;
        this.url = url;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemResponse [id=" + id + ", productName=" + productName + ", url=" + url + ", price=" + price
                + ", quantity=" + quantity + "]";
    }


    

}
