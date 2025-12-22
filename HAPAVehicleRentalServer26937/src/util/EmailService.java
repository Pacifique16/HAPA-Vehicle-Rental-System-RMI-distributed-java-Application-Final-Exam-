package util;

public class EmailService {
    
    public static boolean sendOTPEmail(String toEmail, String otp, String userName) {
        // Try to send real email first
        boolean emailSent = GmailSender.sendOTP(toEmail, otp, userName);
        
        if (emailSent) {
            System.out.println("\n" + repeatChar('#', 70));
            System.out.println("#" + centerText("📧 OTP EMAIL SENT TO YOUR GMAIL!", 68) + "#");
            System.out.println("#" + centerText("📱 Check: " + toEmail, 68) + "#");
            System.out.println("#" + centerText("🔐 OTP: " + otp + " (" + otp.length() + " digits)", 68) + "#");
            System.out.println("#" + centerText("⏰ Valid for 5 minutes", 68) + "#");
            System.out.println(repeatChar('#', 70) + "\n");
        } else {
            // Fallback: Display OTP in console
            System.out.println("\n" + repeatChar('#', 70));
            System.out.println("#" + centerText("📧 EMAIL FAILED - OTP IN CONSOLE", 68) + "#");
            System.out.println("#" + centerText("📱 Email: " + toEmail, 68) + "#");
            System.out.println("#" + centerText("🔐 OTP CODE: " + otp, 68) + "#");
            System.out.println("#" + centerText("⏰ Valid for 5 minutes", 68) + "#");
            System.out.println(repeatChar('#', 70) + "\n");
        }
        
        return true; // Always return true so login process continues
    }
    
    private static String centerText(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(text);
        while (sb.length() < width) sb.append(" ");
        return sb.toString();
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}