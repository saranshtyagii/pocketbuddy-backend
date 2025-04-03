package com.web.pocketbuddy.service.notification;

import com.web.pocketbuddy.exception.NotificationException;
import io.micrometer.common.util.StringUtils;
import io.micrometer.core.instrument.util.IOUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String templateName, String replaceKey, String replaceValue) {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
           helper.setTo(to);
           helper.setSubject(subject);

           String htmlContent = readHtmlTemplate(templateName);
           if(StringUtils.isNotEmpty(replaceValue) && StringUtils.isNotEmpty(replaceKey)) {
               htmlContent = htmlContent.replace(replaceKey, replaceValue);
           }

           helper.setText(htmlContent, true);

           mailSender.send(message);

       } catch (Exception e) {
           throw new NotificationException("Unable to send email please try again later", HttpStatus.BAD_REQUEST);
       }
    }

    private String readHtmlTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + templateName);
            InputStream inputStream = resource.getInputStream();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error reading email template: " + templateName, e);
        }
    }

}
