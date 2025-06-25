package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void mailSend(String to, String token) {
        String subject = "Şifre Sıfırlama Bağlantınız";
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String body = "Şifrenizi sıfırlamak için aşağıdaki bağlantıya tıklayın:\n\n" + resetUrl;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
            System.out.println("Mail başarıyla gönderildi: " + to);
        } catch (MessagingException e) {
            System.err.println("Mail gönderme hatası: " + e.getMessage());
        }
    }

}
