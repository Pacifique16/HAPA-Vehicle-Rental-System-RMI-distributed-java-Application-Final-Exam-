package util;

public class SMTPEmailSender {
    
    public static boolean sendOTP(String toEmail, String otp, String userName) {
        // For demo purposes, display OTP prominently in console
        System.out.println("\n" + repeatChar('=', 60));
        System.out.println("📧 OTP VERIFICATION CODE");
        System.out.println(repeatChar('=', 60));
        System.out.println("📱 Email: " + toEmail);
        System.out.println("👤 Name: " + userName);
        System.out.println("🔐 Your OTP: " + otp);
        System.out.println("⏰ Valid for: 5 minutes");
        System.out.println("📝 Use this code to login");
        System.out.println(repeatChar('=', 60) + "\n");
        return true;
    }
    
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}