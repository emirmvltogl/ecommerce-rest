package com.example.demo.dto;

public class CartItemDTO {
    private Long productId;
    private int quantity;

    // Getter ve Setter'lar
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
