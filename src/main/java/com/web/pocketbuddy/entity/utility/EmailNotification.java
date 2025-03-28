package com.web.pocketbuddy.entity.utility;

import lombok.*;

import java.io.File;

@Data
@Getter
public class EmailNotification {

    private String sendTo;
    private String subject;
    private String messageBody;
    private File attachment;

    public EmailNotification(String sendTo) {
        this.sendTo = sendTo;
    }

    public EmailNotification(String sendTo, String subject, String messageBody, File attachment) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.messageBody = messageBody;
        this.attachment = attachment;
    }

    public EmailNotification(String sendTo, String subject, String messageBody) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.messageBody = messageBody;
    }

}
