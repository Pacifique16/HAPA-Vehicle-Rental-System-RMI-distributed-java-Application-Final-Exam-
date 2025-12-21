package util;

public class EmailConfig {
    
    public static boolean sendEmail(String toEmail, String subject, String body) {
        // Extract user name from email for personalization
        String userName = toEmail.substring(0, toEmail.indexOf('@'));
        
        // For OTP emails, extract the OTP code from the body
        String otp = "";
        if (body.contains("Your OTP verification code is: ")) {
            int startIndex = body.indexOf("Your OTP verification code is: ") + 32;
            int endIndex = body.indexOf("\n", startIndex);
            if (endIndex == -1) {
                // If no newline found, look for the next space or end of string
                endIndex = body.indexOf(" ", startIndex);
                if (endIndex == -1) endIndex = body.length();
            }
            otp = body.substring(startIndex, endIndex).trim();
            // Ensure we only get the numeric part
            otp = otp.replaceAll("[^0-9]", "");
        }
        
        // Use the real email service
        if (!otp.isEmpty()) {
            return EmailService.sendOTPEmail(toEmail, otp, userName);
        } else {
            // Fallback for non-OTP emails
            System.out.println("\n=== EMAIL SENT ===");
            System.out.println("To: " + toEmail);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("==================\n");
            return true;
        }
    }
}