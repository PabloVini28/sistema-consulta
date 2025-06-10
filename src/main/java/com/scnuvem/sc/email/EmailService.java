package com.scnuvem.sc.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(EMAIL_SENDER);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Email confirm");
        String htmlContent = getMessage(code);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendForgotPasswordEmail(String to, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Password reset");
        String htmlContent = getMessageForgotPassword(resetLink);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendVerifyMessage(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Email Verified");
        String htmlContent = getVerifyMessage();
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private String getMessage(String code){
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Email Confirmation</title></head>" +
                "<body>" +
                "<h2>Email Confirmation</h2>" +
                "<p>Hello,</p>" +
                "<p>Your verification code is:</p>" +
                "<h1>" + code + "</h1>" +
                "<p>Please enter this code to complete your registration.</p>" +
                "<p>If you did not request this code, please ignore this email.</p>" +
                "<hr>" +
                "<footer><p>This is an automated email, please do not reply.</p></footer>" +
                "</body>" +
                "</html>";
        return htmlContent;
    }

    private String getMessageForgotPassword(String resetLink){
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Password Recovery</title></head>" +
                "<body>" +
                "<h2>Password Recovery</h2>" +
                "<p>Hello,</p>" +
                "<p>If you requested a recovery, click the link to reset your password</p>" +
                "<a href=" + resetLink + ">" + resetLink + "</a>" +
                "<p>If you did not request this recovery, please ignore this email.</p>" +
                "<hr>" +
                "<footer><p>This is an automated email, please do not reply.</p></footer>" +
                "</body>" +
                "</html>";
        return htmlContent;
    }

    private String getVerifyMessage(){
        String htmlContent = "<!DOCTYPE html>" +
        "<html>" +
        "<head>" +
            "<title>Email Verified</title>" +
        "</head>" +
        "<body>" +
            "<h2>Email Successfully Verified</h2>" +
            "<p>Hello,</p>" +
            "<p>Your email address has been successfully verified.</p>" +
            "<p>You can now access all features of our platform.</p>" +
            "<p>If this was not you, please contact our support team immediately.</p>" +
            "<hr>" +
            "<footer>" +
                "<p>This is an automated email, please do not reply.</p>" +
            "</footer>" +
        "</body>" +
        "</html>";
        return htmlContent;
    }

}
