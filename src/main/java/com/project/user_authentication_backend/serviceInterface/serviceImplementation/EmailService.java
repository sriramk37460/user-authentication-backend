package com.project.user_authentication_backend.serviceInterface.serviceImplementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message
        // Set your sender email here
        helper.setFrom("");
        helper.setTo(to);
        helper.setSubject(subject);
        // Set to true if you want to send HTML content
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}
