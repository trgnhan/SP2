package com.nhan.sp2.service.impl;

import com.nhan.sp2.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailServiceImpl implements EmailService {

    @Value("${spring.sendgrid.from-email}")
    private String fromE;

    private final SendGrid sendGrid;

    /**
     * Send email by SendGrid
     * @param to send email to someone
     * @param subject
     * @param text
     */
    public void sendMail(String to, String subject, String text) {
        Email fromEmail = new Email(fromE);
        Email toEmail = new Email(to);

        Content content = new Content("text/plain",text );
        Mail mail = new Mail(fromEmail,subject,toEmail,content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {  // Accepted
                log.info("Email sent successfully");
            } else {
                log.error("Email sent failed");
            }
            // In ra mã trạng thái và thông báo lỗi chi tiết từ SendGrid
            log.info("SendGrid Status Code: {}", response.getStatusCode());
            log.info("SendGrid Response Body: {}", response.getBody());

            // Nếu status code >= 400 nghĩa là có lỗi xảy ra
            if (response.getStatusCode() >= 400) {
                log.error("Chi tiết lỗi từ SendGrid: {}", response.getBody());
                throw new RuntimeException("Lỗi từ SendGrid API, kiểm tra log để biết chi tiết");
            }

        } catch (IOException e) {
            log.error("Email sent failed, error: {}",e.getMessage());
        }
    }
}
