package service.implementation;

import service.OTPService;
import util.EmailConfig;
import dao.UserDAOImpl;
import model.User;
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
        try {
            UserDAOImpl userDAO = new UserDAOImpl();
            User user = userDAO.getUserByUsername(username);
            
            if (user == null) {
                throw new RemoteException("User not found: " + username);
            }
            
            Random random = new Random();
            int otpNumber = random.nextInt(90000) + 10000;
            String otp = String.format("%05d", otpNumber);
            
            otpStorage.put(username, otp);
            otpTimestamp.put(username, System.currentTimeMillis());
            
            boolean emailSent = util.GmailSender.sendOTP(user.getEmail(), otp, user.getFullName());
            if (!emailSent) {
                throw new RemoteException("Failed to send OTP email");
            }
            
            return otp;
        } catch (Exception e) {
            throw new RemoteException("Failed to generate and send OTP: " + e.getMessage());
        }
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
        return util.GmailSender.sendOTP(email, otp, "Valued Customer");
    }
    
    @Override
    public boolean sendOTPEmail(String email, String otp, String fullName) throws RemoteException {
        return util.GmailSender.sendOTP(email, otp, fullName);
    }
}