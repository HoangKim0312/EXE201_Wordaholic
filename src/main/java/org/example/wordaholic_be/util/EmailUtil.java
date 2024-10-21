package org.example.wordaholic_be.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("Your OTP for verification is: " + otp, true); // OTP in email body

        javaMailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(String to, String resetLink) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true); // Enable HTML content
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Password Reset Request");
        mimeMessageHelper.setText("""
                <html>
                <body>
                  <p>To reset your password, click the following link:</p>
                  <a href="%s">Reset Password</a>
                </body>
                </html>
                """.formatted(resetLink), true); // Set the content as HTML

        javaMailSender.send(mimeMessage);
    }
}
