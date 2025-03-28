package com.web.pocketbuddy.service.notification;

import com.web.pocketbuddy.entity.utility.EmailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService{


    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(EmailNotification emailNotification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailNotification.getSendTo());
            message.setSubject(emailNotification.getSubject());
            message.setText(emailNotification.getMessageBody());
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public static void sendSms(String to, String subject, String body) {}

    public static void sendInAppNotification(String to, String subject, String body) {}

}
