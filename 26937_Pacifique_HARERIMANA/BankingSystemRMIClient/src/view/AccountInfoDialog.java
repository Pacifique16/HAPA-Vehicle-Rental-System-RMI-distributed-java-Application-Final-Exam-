package view;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import model.*;
import service.AccountService;

public class AccountInfoDialog extends javax.swing.JDialog {

    public AccountInfoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        accountNumberField = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Account Information");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("ACCOUNT INFORMATION");

        jLabel2.setText("Account Number:");

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        infoArea.setEditable(false);
        infoArea.setColumns(20);
        infoArea.setRows(5);
        jScrollPane1.setViewportView(infoArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(accountNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchBtn))
                    .addComponent(jLabel1))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(accountNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String accNum = accountNumberField.getText().trim();
        if(accNum.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter account number");
            return;
        }
        
        try{
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 3000);
            AccountService service = (AccountService) reg.lookup("account");
            
            Account acc = new Account(accNum);
            Account account = service.getAccountWithDetails(acc);
            
            if(account == null){
                JOptionPane.showMessageDialog(this, "Account not found");
                return;
            }
            
            System.out.println("Customers: " + (account.getCustomers() != null ? account.getCustomers().size() : "null"));
            System.out.println("Cards: " + (account.getCards() != null ? account.getCards().size() : "null"));
            System.out.println("Loans: " + (account.getLoans() != null ? account.getLoans().size() : "null"));
            if(account.getLoans() != null){
                for(Loan loan : account.getLoans()){
                    System.out.println("  Loan " + loan.getId() + " repayments: " + (loan.getRepayments() != null ? loan.getRepayments().size() : "null"));
                }
            }
            
            StringBuilder info = new StringBuilder();
            info.append("=== ACCOUNT DETAILS ===\n\n");
            info.append("Account Number: ").append(account.getAccountNumber()).append("\n");
            info.append("Balance: ").append(account.getBalance()).append("\n");
            info.append("Type: ").append(account.getType()).append("\n");
            info.append("Status: ").append(account.isActive() ? "Active" : "Inactive").append("\n");
            info.append("Registered: ").append(account.getRegisteredDate()).append("\n\n");
            
            info.append("=== CUSTOMER DETAILS ===\n\n");
            if(account.getCustomers() != null && !account.getCustomers().isEmpty()){
                for(Customer cust : account.getCustomers()){
                    info.append("Name: ").append(cust.getFirstName()).append(" ").append(cust.getLastName()).append("\n");
                    info.append("Email: ").append(cust.getEmail()).append("\n");
                    info.append("Phone: ").append(cust.getPhoneNumber()).append("\n");
                    info.append("Address: ").append(cust.getAddress()).append("\n");
                    info.append("DOB: ").append(cust.getDob()).append("\n");
                    info.append("Gender: ").append(cust.getGender()).append("\n");
                    if(cust.getRiskProfile() != null){
                        info.append("Risk Profile: ").append(cust.getRiskProfile().getType()).append("\n");
                    }
                    info.append("\n");
                }
            }
            
            info.append("=== CARDS ===\n\n");
            if(account.getCards() != null && !account.getCards().isEmpty()){
                for(Card card : account.getCards()){
                    info.append("Card Number: ").append(card.getCardNumber()).append("\n");
                    info.append("Type: ").append(card.getType()).append("\n");
                    info.append("Expiry: ").append(card.getEndMonth()).append("/").append(card.getEndYear()).append("\n");
                    info.append("Status: ").append(card.isActive() ? "Active" : "Blocked").append("\n\n");
                }
            } else {
                info.append("No cards issued\n\n");
            }
            
            info.append("=== LOANS ===\n\n");
            if(account.getLoans() != null && !account.getLoans().isEmpty()){
                double totalRemaining = 0;
                for(Loan loan : account.getLoans()){
                    double paid = 0;
                    if(loan.getRepayments() != null){
                        for(LoanRepayment rep : loan.getRepayments()){
                            paid += rep.getPaymentAmount();
                        }
                    }
                    double remaining = loan.getAmountToPay() - paid;
                    info.append("Loan ID: ").append(loan.getId()).append("\n");
                    info.append("Amount Received: ").append(loan.getAmountToReceive()).append("\n");
                    info.append("Total to Pay: ").append(loan.getAmountToPay()).append("\n");
                    info.append("Amount Paid: ").append(paid).append("\n");
                    info.append("Remaining: ").append(remaining).append("\n");
                    info.append("Status: ").append(loan.getStatus()).append("\n");
                    info.append("Start Date: ").append(loan.getStartDate()).append("\n");
                    info.append("End Date: ").append(loan.getEndDate()).append("\n\n");
                    if(loan.getStatus() != ELoanStatus.COMPLETED){
                        totalRemaining += remaining;
                    }
                }
                info.append("Total Remaining Loan: ").append(totalRemaining).append("\n\n");
            } else {
                info.append("No loans\n\n");
            }
            
            infoArea.setText(info.toString());
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField accountNumberField;
    private javax.swing.JButton searchBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea infoArea;
}
