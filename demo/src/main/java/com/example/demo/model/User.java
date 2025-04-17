package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user") // Kullanıcı tablosu adını özelleştirdim
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username", nullable = false, unique = true) // Kullanıcı adı benzersiz olmalı
  private String username;

  @Column(name = "password", nullable = false)
  @ToString.Exclude // Hassas bilgiyi toString'den hariç tut
  private String password;

  @Column(name = "email", nullable = false, unique = true) // E-posta benzersiz olmalı
  private String email;

  @Column(name = "reset_token")
  private String resetToken;

  @Column(name = "token_expiry_date")
  private LocalDateTime tokenExpiryDate;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  public User(String username, String password, boolean enabled) {
    this.username = username;
    this.password = password;
    this.enabled = enabled;
  }

  public User(String username, String password, String email, boolean enabled, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.enabled = enabled;
    this.email = email;
    this.roles = roles;
  }

  // Constructor for password reset (includes resetToken and tokenExpiryDate)
  public User(String username, String email, String resetToken, LocalDateTime tokenExpiryDate) {
    this.username = username;
    this.email = email;
    this.resetToken = resetToken;
    this.tokenExpiryDate = tokenExpiryDate;
  }
  
}