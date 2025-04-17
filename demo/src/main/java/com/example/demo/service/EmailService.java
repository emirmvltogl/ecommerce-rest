package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
  
  private JavaMailSender mailSender;

  @Autowired
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }


  public void mailSend(String recipientEmail, String resetToken) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("eogemir@gmail.com");
        helper.setTo(recipientEmail); // Bu satır null olmamalı
        helper.setSubject("Parola Sıfırlama İsteği");
        helper.setText("Parola sıfırlama linkiniz: http://localhost:8080/pas/reset?token=" + resetToken, true);

        mailSender.send(message);
    } catch (MessagingException e) {
        throw new IllegalStateException("E-posta gönderimi başarısız oldu", e);
    }
}

}
