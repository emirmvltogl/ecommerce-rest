package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import com.example.demo.util.TokenUtil;

@Service
public class PasswordResetService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    @Autowired
    public PasswordResetService(UserRepo userRepo, BCryptPasswordEncoder encoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    /**
     * Parola sıfırlama token'ı oluşturur ve e-posta gönderir.
     * 
     * @param email Kullanıcının e-posta adresi
     * @return Oluşturulan token
     * @throws IllegalArgumentException Eğer kullanıcı bulunamazsa
     */
    public String createPasswordResetToken(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-posta adresi bulunamadi: " + email));

        // Token oluşturma
        String token = TokenUtil.generateResetToken();
        user.setResetToken(encoder.encode(token)); // Token hashlenerek saklanıyor
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1)); // Token süresi 1 saat

        userRepo.save(user);

        // Token ile e-posta gönderme
        emailService.mailSend(email, token);

        return token;
    }

    /**
     * Token'ın geçerliliğini doğrular.
     * 
     * @param token Kullanıcının sıfırlama token'ı
     * @return Doğrulanan kullanıcı
     * @throws IllegalArgumentException Eğer token geçersiz veya süresi dolmuşsa
     */
    public User validateResetToken(String token) {
        User user = userRepo.findAll().stream()
                .filter(u -> encoder.matches(token, u.getResetToken()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz veya süresi dolmuş token"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token süresi dolmuş");
        }
        return user;
    }

    /**
     * Parola sıfırlama işlemini gerçekleştirir.
     * 
     * @param token       Kullanıcının sıfırlama token'ı
     * @param newPassword Yeni parola
     * @throws IllegalArgumentException Eğer token geçersizse
     */
    public void resetPassword(String token, String newPassword) {
        User user = validateResetToken(token);
        user.setPassword(encoder.encode(newPassword));
        System.out.println("New Password is : "+newPassword);
        user.setResetToken(null); // Token sıfırlandıktan sonra temizlenir
        user.setTokenExpiryDate(null);

        userRepo.save(user);
    }
}
