package com.kanchan.studentmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendStudentWelcomeEmail(String to, String studentName) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Welcome to Student Management System");

        message.setText(
                "Dear " + studentName + ",\n\n"
              + "Welcome to Student Management System.\n\n"
              + "Your registration has been completed successfully.\n\n"
              + "Thank You!"
        );

        mailSender.send(message);
    }
    public void sendOtpEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Password Reset OTP");

        message.setText(
                "Dear User,\n\n"
            + "Your OTP for password reset is : " + otp
            + "\n\nThis OTP is valid for a short time."
            + "\n\nThank You!"
        );

        mailSender.send(message);
    }
}