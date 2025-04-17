package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.PasswordResetService;

@RestController
@RequestMapping("/pas")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @Autowired
  public PasswordResetController(PasswordResetService passwordResetService) {
    this.passwordResetService = passwordResetService;
  }

  /**
   * Parola sıfırlama isteği oluşturur.
   * 
   * @param request Kullanıcının e-posta adresini içerir
   * @return Başarı mesajı
   */
  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPasswordRequest(@Validated @RequestBody ResetPasswordRequest request) {
    passwordResetService.createPasswordResetToken(request.getEmail());
    return ResponseEntity.ok("Parola sıfırlama isteği oluşturuldu. Lütfen e-postanızı kontrol edin.");
  }

  /**
   * Parolayı sıfırlar.
   * 
   * @param token       Kullanıcının sıfırlama token'ı
   * @param newPassword Yeni parola
   * @return Başarı mesajı
   */
  @PostMapping("/reset")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody @jakarta.validation.constraints.NotBlank String newPassword) {
    passwordResetService.resetPassword(token, newPassword);
    return ResponseEntity.ok("Parolanız başarıyla sıfırlandı.");
  }

  // İç sınıf: ResetPasswordRequest (DTO)
  public static class ResetPasswordRequest {
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Email
    private String email;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }
}
