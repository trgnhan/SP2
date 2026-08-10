package com.nhan.sp2.service;

public interface EmailService {
    void sendMail(String to, String subject, String text);
}
