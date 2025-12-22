package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OTPService extends Remote {
    String generateOTP(String username) throws RemoteException;
    boolean validateOTP(String username, String otp) throws RemoteException;
    boolean sendOTPEmail(String email, String otp) throws RemoteException;
    boolean sendOTPEmail(String email, String otp, String fullName) throws RemoteException;
}