package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.CartResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserDetailsService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserDetailsService userService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final OrderService orderService;

    @Autowired
    public CartController(CartService cartService, UserDetailsService userService, OrderService orderService) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
    }

    // Kullanıcının sepetini al ve göster
    @GetMapping("")
    public ResponseEntity<CartResponse> showCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(new CartResponse("Kullanıcı girişi yapılmamış"));
        }
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        var cart = cartService.getCartByUser(user);
        CartResponse response = new CartResponse(cart, cart.getTotalPrice());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartItemDTO cartItemDTO) {

        // Konsola yazdır
        System.out.println("Ürün ID: " + cartItemDTO.getProductId());
        System.out.println("Adet: " + cartItemDTO.getQuantity());
        logger.info("Sepete eklenecek ürün - ID: {}, Adet: {}", cartItemDTO.getProductId(), cartItemDTO.getQuantity());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CartResponse("User not found"));
        }

        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Sepete öğe ekleme işlemi
        cartService.addItemToCart(user, cartItemDTO.getProductId(), cartItemDTO.getQuantity());

        // Kullanıcıya ait sepeti alıyoruz
        Cart cart = cartService.getCartByUser(user);

        // Sepet detaylarını DTO'ya çeviriyoruz
        CartResponse response = new CartResponse(cart, cart.getTotalPrice());

        return ResponseEntity.ok(response);
    }

    // Sepetten ürün çıkarma
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        System.out.println("Silme isteği geldi: " + productId);

        if (userDetails == null) {
            return ResponseEntity.badRequest().body(new CartResponse("Kullanıcı girişi yapılmamış"));
        }
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        cartService.removeItemFromCart(user, productId);
        var cart = cartService.getCartByUser(user);
        CartResponse response = new CartResponse(cart, cart.getTotalPrice());
        return ResponseEntity.ok(response);
    }

    // Sepeti temizleme
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(new CartResponse("Kullanıcı girişi yapılmamış"));
        }
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        cartService.clearCart(user);
        var cart = cartService.getCartByUser(user);
        CartResponse response = new CartResponse(cart, cart.getTotalPrice());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı girişi yapılmamış");
        }

        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        Cart cart = cartService.getCartByUser(user);
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Sepet boş");
        }

        orderService.createOrderFromCart(cart); // Siparişi oluştur
        cartService.clearCart(user); // Sepeti temizle

        return ResponseEntity.ok("Satın alma işlemi tamamlandı!");
    }


}
