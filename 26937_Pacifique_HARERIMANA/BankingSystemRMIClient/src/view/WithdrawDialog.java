package view;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import javax.swing.*;
import model.*;
import service.*;

public class WithdrawDialog extends JDialog {
    private JTextField accountNumberTxt, amountTxt;
    private JLabel currentBalanceLabel;
    
    public WithdrawDialog(Frame parent) {
        super(parent, "WITHDRAW MONEY", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("CUSTOMER WITHDRAWAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accountNumberTxt = new JTextField(20);
        panel.add(accountNumberTxt, gbc);
        
        JButton checkBtn = new JButton("Check Account");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(checkBtn, gbc);
        
        currentBalanceLabel = new JLabel("Current Balance: ---");
        currentBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentBalanceLabel.setForeground(new Color(204, 0, 0));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(currentBalanceLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Withdrawal Amount:"), gbc);
        gbc.gridx = 1;
        amountTxt = new JTextField(20);
        panel.add(amountTxt, gbc);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton withdrawBtn = new JButton("WITHDRAW");
        JButton cancelBtn = new JButton("CANCEL");
        
        withdrawBtn.setBackground(new Color(204, 0, 0));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnPanel.add(withdrawBtn);
        btnPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);
        
        add(panel);
        
        checkBtn.addActionListener(e -> checkAccount());
        withdrawBtn.addActionListener(e -> withdraw());
        cancelBtn.addActionListener(e -> dispose());
    }
    
    private void checkAccount() {
        String accountNumber = accountNumberTxt.getText().trim();
        if(accountNumber.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter account number!");
            return;
        }
        
        try{
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 3000);
            AccountService service = (AccountService)registry.lookup("account");
            Account account = new Account(accountNumber);
            account = service.searchAccountById(account);
            
            if(account != null){
                currentBalanceLabel.setText("Current Balance: $" + account.getBalance());
                if(!account.isActive()){
                    JOptionPane.showMessageDialog(this, "WARNING: Account is INACTIVE!");
                }
            }else{
                currentBalanceLabel.setText("Current Balance: ---");
                JOptionPane.showMessageDialog(this, "Account not found!");
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void withdraw() {
        if(accountNumberTxt.getText().trim().isEmpty() || amountTxt.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }
        
        try{
            double amount = Double.parseDouble(amountTxt.getText().trim());
            if(amount <= 0){
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero!");
                return;
            }
            
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 3000);
            TransactionService service = (TransactionService)registry.lookup("transaction");
            
            Transaction transaction = new Transaction();
            transaction.setType(ETransactionType.WITHDRAW);
            transaction.setAmount(amount);
            transaction.setTransactionDate(LocalDate.now());
            Account account = new Account(accountNumberTxt.getText().trim());
            transaction.setAccount(account);
            
            Transaction result = service.registerTransaction(transaction);
            
            if(result != null){
                AccountService accService = (AccountService)registry.lookup("account");
                Account updatedAccount = accService.searchAccountById(account);
                
                JOptionPane.showMessageDialog(this,
                    "WITHDRAWAL SUCCESSFUL!\n\n" +
                    "Amount Withdrawn: $" + amount + "\n" +
                    "New Balance: $" + updatedAccount.getBalance() + "\n\n" +
                    "Give cash to customer!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, 
                "WITHDRAWAL FAILED!\n\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
