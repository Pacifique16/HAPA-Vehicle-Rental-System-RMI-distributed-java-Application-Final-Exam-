package view;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import model.*;
import service.*;

public class RiskProfileDialog extends javax.swing.JDialog {

    public RiskProfileDialog(java.awt.Frame parent, boolean modal) {
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
        jLabel3 = new javax.swing.JLabel();
        customerNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        riskTypeCombo = new javax.swing.JComboBox<>();
        setRiskBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Set Risk Profile");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("SET RISK PROFILE");

        jLabel2.setText("Account Number:");

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        jLabel3.setText("Customer Name:");

        customerNameField.setEditable(false);

        jLabel4.setText("Risk Type:");

        riskTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LOW", "MEDIUM", "HIGH" }));

        setRiskBtn.setText("Set Risk Profile");
        setRiskBtn.setEnabled(false);
        setRiskBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setRiskBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(accountNumberField)
                            .addComponent(customerNameField)
                            .addComponent(riskTypeCombo, 0, 200, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchBtn)
                            .addComponent(setRiskBtn))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(accountNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(customerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(riskTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setRiskBtn))
                .addContainerGap(30, Short.MAX_VALUE))
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

    private Customer currentCustomer = null;

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
            
            if(account.getCustomers() == null || account.getCustomers().isEmpty()){
                JOptionPane.showMessageDialog(this, "No customer linked to this account");
                return;
            }
            
            currentCustomer = account.getCustomers().iterator().next();
            customerNameField.setText(currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
            
            if(currentCustomer.getRiskProfile() != null){
                riskTypeCombo.setSelectedItem(currentCustomer.getRiskProfile().getType().toString());
            }
            
            setRiskBtn.setEnabled(true);
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setRiskBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if(currentCustomer == null){
            JOptionPane.showMessageDialog(this, "Search for an account first");
            return;
        }
        
        try{
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 3000);
            RiskProfileService service = (RiskProfileService) reg.lookup("riskProfile");
            
            ERiskType riskType = ERiskType.valueOf(riskTypeCombo.getSelectedItem().toString());
            
            RiskProfile profile;
            if(currentCustomer.getRiskProfile() != null){
                profile = currentCustomer.getRiskProfile();
                profile.setType(riskType);
                service.updateRiskProfile(profile);
                JOptionPane.showMessageDialog(this, "Risk profile updated successfully");
            } else {
                profile = new RiskProfile();
                profile.setType(riskType);
                profile.setCustomer(currentCustomer);
                service.registerRiskProfile(profile);
                JOptionPane.showMessageDialog(this, "Risk profile created successfully");
            }
            
            currentCustomer = null;
            customerNameField.setText("");
            accountNumberField.setText("");
            setRiskBtn.setEnabled(false);
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField accountNumberField;
    private javax.swing.JTextField customerNameField;
    private javax.swing.JButton searchBtn;
    private javax.swing.JComboBox<String> riskTypeCombo;
    private javax.swing.JButton setRiskBtn;
}
