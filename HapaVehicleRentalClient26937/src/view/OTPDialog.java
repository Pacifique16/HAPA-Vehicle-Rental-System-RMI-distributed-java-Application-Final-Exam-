package view;

import service.OTPService;
import java.rmi.Naming;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OTPDialog extends JDialog {
    private JTextField otpField;
    private JButton verifyButton;
    private JButton resendButton;
    private boolean verified = false;
    private String username;
    private String userEmail;
    
    public OTPDialog(Frame parent, String username, String userEmail) {
        super(parent, "OTP Verification", true);
        this.username = username;
        this.userEmail = userEmail;
        initComponents();
        sendOTP();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(400, 200);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Enter OTP sent to: " + userEmail), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("OTP:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        otpField = new JTextField(10);
        mainPanel.add(otpField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> verifyOTP());
        mainPanel.add(verifyButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        resendButton = new JButton("Resend OTP");
        resendButton.addActionListener(e -> sendOTP());
        mainPanel.add(resendButton, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Auto-focus on OTP field
        otpField.requestFocus();
    }
    
    private void sendOTP() {
        try {
            OTPService otpService = (OTPService) Naming.lookup("rmi://localhost:3506/OTPService");
            String otp = otpService.generateOTP(username);
            otpService.sendOTPEmail(userEmail, otp);
            JOptionPane.showMessageDialog(this, "OTP sent to " + userEmail + "\\nFor demo: " + otp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage());
        }
    }
    
    private void verifyOTP() {
        String otp = otpField.getText().trim();
        if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter OTP");
            return;
        }
        
        try {
            OTPService otpService = (OTPService) Naming.lookup("rmi://localhost:3506/OTPService");
            if (otpService.validateOTP(username, otp)) {
                verified = true;
                JOptionPane.showMessageDialog(this, "OTP verified successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid or expired OTP");
                otpField.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verification failed: " + e.getMessage());
        }
    }
    
    public boolean isVerified() {
        return verified;
    }
}