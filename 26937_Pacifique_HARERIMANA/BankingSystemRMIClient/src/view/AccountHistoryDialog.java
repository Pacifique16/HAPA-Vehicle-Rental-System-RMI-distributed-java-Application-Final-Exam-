package view;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.*;
import java.util.List;

public class AccountHistoryDialog extends JDialog {
    private JTextField accountNumberTxt;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel accountInfoLabel;
    
    public AccountHistoryDialog(Frame parent) {
        super(parent, "ACCOUNT HISTORY", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("ACCOUNT TRANSACTION HISTORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        topPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accountNumberTxt = new JTextField(20);
        topPanel.add(accountNumberTxt, gbc);
        
        JButton loadBtn = new JButton("LOAD HISTORY");
        loadBtn.setBackground(new Color(0, 102, 204));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2;
        topPanel.add(loadBtn, gbc);
        
        accountInfoLabel = new JLabel("Account: ---");
        accountInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        topPanel.add(accountInfoLabel, gbc);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        String[] columns = {"Date", "Type", "Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        tableModel.setColumnIdentifiers(columns);
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transactions"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton closeBtn = new JButton("CLOSE");
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        loadBtn.addActionListener(e -> loadHistory());
        closeBtn.addActionListener(e -> dispose());
        accountNumberTxt.addActionListener(e -> loadHistory());
    }
    
    private void loadHistory() {
        String accountNumber = accountNumberTxt.getText().trim();
        if(accountNumber.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter account number!");
            return;
        }
        
        tableModel.setRowCount(0);
        
        try{
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 3000);
            
            AccountService accService = (AccountService)registry.lookup("account");
            Account account = new Account(accountNumber);
            account = accService.searchAccountById(account);
            
            if(account == null){
                JOptionPane.showMessageDialog(this, "Account not found!");
                accountInfoLabel.setText("Account: ---");
                return;
            }
            
            accountInfoLabel.setText("Account: " + account.getAccountNumber() + " | Balance: $" + account.getBalance() + " | Type: " + account.getType());
            
            TransactionService txService = (TransactionService)registry.lookup("transaction");
            List<Transaction> transactions = txService.retrieveAllTransactions();
            
            int count = 0;
            if(transactions != null){
                for(Transaction tx : transactions){
                    if(tx.getAccount() != null && tx.getAccount().getAccountNumber().equals(accountNumber)){
                        Object[] row = {
                            tx.getTransactionDate(),
                            tx.getType(),
                            "$" + tx.getAmount(),
                            tx.getStatus()
                        };
                        tableModel.addRow(row);
                        count++;
                    }
                }
            }
            
            Account fullAccount = accService.getAccountWithDetails(account);
            if(fullAccount != null && fullAccount.getLoans() != null){
                for(Loan loan : fullAccount.getLoans()){
                    double totalPaid = 0;
                    if(loan.getRepayments() != null){
                        for(LoanRepayment rep : loan.getRepayments()){
                            totalPaid += rep.getPaymentAmount();
                        }
                    }
                    double remaining = loan.getAmountToPay() - totalPaid;
                    
                    Object[] loanRow = {
                        loan.getCreatedDate(),
                        "LOAN_DISBURSEMENT (Total: $" + loan.getAmountToPay() + ")",
                        "$" + loan.getAmountToReceive(),
                        "Remaining: $" + loan.getAmountToPay()
                    };
                    tableModel.addRow(loanRow);
                    count++;
                    
                    if(loan.getRepayments() != null){
                        double paidSoFar = 0;
                        for(LoanRepayment rep : loan.getRepayments()){
                            paidSoFar += rep.getPaymentAmount();
                            double remainingAfter = loan.getAmountToPay() - paidSoFar;
                            Object[] repayRow = {
                                rep.getPaymentDate(),
                                "LOAN_REPAYMENT",
                                "$" + rep.getPaymentAmount(),
                                "Remaining: $" + remainingAfter
                            };
                            tableModel.addRow(repayRow);
                            count++;
                        }
                    }
                }
            }
            
            if(count == 0){
                JOptionPane.showMessageDialog(this, "No transactions found for this account.");
            }
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
