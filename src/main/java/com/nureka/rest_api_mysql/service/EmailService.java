package com.nureka.rest_api_mysql.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String subject, String text);

    void sendEmailWithAttachment(String to, String subject, String text, String attachmentName, String pathToAttachment) throws MessagingException;

    void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;
}