package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;
    private double totalPrice;

    public CartResponse(Cart cart, double totalPrice) {
        this.cartId = cart.getId();
        this.totalPrice = totalPrice;
        // Varlıktaki CartItem listesini DTO'ya çeviriyoruz
        this.items = cart.getItems().stream()
            .map(this::mapItem)
            .collect(Collectors.toList());
    }

    public CartResponse(String string) {
    }

    private CartItemResponse mapItem(CartItem item) {
        return new CartItemResponse(
            item.getId(),
            item.getProduct().getProductName(),
            item.getProduct().getUrl(),
            item.getProduct().getPrice(),
            item.getQuantity()
        );
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartResponse [cartId=" + cartId + ", items=" + items + ", totalPrice=" + totalPrice + "]";
    }

    


}
