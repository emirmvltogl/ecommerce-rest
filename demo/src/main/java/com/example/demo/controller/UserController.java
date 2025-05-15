package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserDetailsService;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserDetailsService userService;

  // ─── ADMIN ────────────────────────────────────────────────────

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    User u = userService.findByUsername(username);
    return u != null
        ? ResponseEntity.ok(u)
        : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody User u) {
    return new ResponseEntity<>(userService.saveUser(u), HttpStatus.CREATED);
  }

  // Eğer “default” register da ADMIN’e bağlı kalacaksa:
  @PostMapping("/default")
  public ResponseEntity<User> registerDefault(@RequestBody User u) {
    return new ResponseEntity<>(userService.saveDefaultUser(u), HttpStatus.CREATED);
  }

  // Rolleri döndürme endpoint'i
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/roles")
  public ResponseEntity<List<String>> getRoles() {
    List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"); // Önceden belirlenmiş roller
    return ResponseEntity.ok(roles);
  }

}
