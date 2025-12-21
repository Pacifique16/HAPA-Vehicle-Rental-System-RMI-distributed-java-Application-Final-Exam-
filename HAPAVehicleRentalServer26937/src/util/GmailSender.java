package util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class GmailSender {
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL = "harerimanapacifique95@gmail.com";
    private static final String APP_PASSWORD = "rlty yrlz jbaz gthu";
    
    public static boolean sendOTP(String toEmail, String otp, String userName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, APP_PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL, "HAPA Vehicle Rental"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("HAPA Vehicle Rental - OTP Verification");
            
            // Use plain text instead of HTML to prevent truncation
            String textContent = 
                "HAPA Vehicle Rental - OTP Verification\n\n" +
                "Dear " + userName + ",\n\n" +
                "Your OTP verification code is: " + otp + "\n\n" +
                "This code will expire in 5 minutes.\n\n" +
                "If you didn't request this code, please ignore this email.\n\n" +
                "Best regards,\n" +
                "HAPA Vehicle Rental Team";
            
            message.setText(textContent);
            
            Transport.send(message);
            
            System.out.println("✅ OTP email sent successfully to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}