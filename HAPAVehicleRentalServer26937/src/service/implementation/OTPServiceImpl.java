package service.implementation;

import service.OTPService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPServiceImpl extends UnicastRemoteObject implements OTPService {
    
    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, Long> otpTimestamp = new HashMap<>();
    private static final long OTP_VALIDITY = 5 * 60 * 1000; // 5 minutes
    
    public OTPServiceImpl() throws RemoteException {
        super();
    }
    
    @Override
    public String generateOTP(String username) throws RemoteException {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(username, otp);
        otpTimestamp.put(username, System.currentTimeMillis());
        System.out.println("Generated OTP for " + username + ": " + otp);
        return otp;
    }
    
    @Override
    public boolean validateOTP(String username, String otp) throws RemoteException {
        String storedOTP = otpStorage.get(username);
        Long timestamp = otpTimestamp.get(username);
        
        if (storedOTP == null || timestamp == null) {
            return false;
        }
        
        // Check if OTP is expired
        if (System.currentTimeMillis() - timestamp > OTP_VALIDITY) {
            otpStorage.remove(username);
            otpTimestamp.remove(username);
            return false;
        }
        
        boolean isValid = storedOTP.equals(otp);
        if (isValid) {
            otpStorage.remove(username);
            otpTimestamp.remove(username);
        }
        
        return isValid;
    }
    
    @Override
    public boolean sendOTPEmail(String email, String otp) throws RemoteException {
        // Simulate email sending
        System.out.println("Sending OTP " + otp + " to email: " + email);
        return true;
    }
}