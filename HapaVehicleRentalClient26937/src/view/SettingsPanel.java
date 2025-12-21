/*
 * HAPA Vehicle Rental System - Settings Panel
 * Provides user profile management and password change functionality
 * Features tabbed interface for profile editing and secure password updates
 * Includes comprehensive validation and user-friendly error handling
 */
package view;

/**
 * SettingsPanel - User settings and profile management interface
 * Provides tabbed interface for profile editing and password management
 * Features secure password validation and profile update functionality
 *
 * @author Pacifique Harerimana
 */

import javax.swing.JPanel;
import model.User;
import service.UserService;
import java.rmi.Naming;
import util.PasswordValidator;

import javax.swing.*;
import java.awt.*;

/**
 * Settings panel with tabbed interface for user profile and password management
 * Provides secure and user-friendly settings management functionality
 */
public class SettingsPanel extends JPanel {

    // User data
    private final User user;          // Current logged-in user

    // Tab navigation buttons
    private JButton btnProfileTab;    // Profile settings tab button
    private JButton btnPasswordTab;   // Password change tab button

    // Panel components
    private JPanel cardsPanel;        // CardLayout container for tab content
    private JPanel profilePanel;      // Profile editing panel
    private JPanel passwordPanel;     // Password change panel

    /**
     * Constructor - Creates settings panel for specified user
     * Initializes tabbed interface with profile and password management
     * 
     * @param user The user whose settings are being managed
     */
    public SettingsPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        buildUI();
    }

    /**
     * Builds the main UI with tabbed interface
     * Creates tab buttons and card layout for content switching
     */
    private void buildUI() {

        // Create top tab navigation area
        JPanel tabs = new JPanel(new GridLayout(1, 2));
        tabs.setBackground(new Color(199, 224, 199));
        tabs.setPreferredSize(new Dimension(100, 60)); // Taller tabs for better visibility

        // Create tab buttons
        btnProfileTab = new JButton("Profile");
        btnPasswordTab = new JButton("Change Password");

        // Style tabs (profile selected by default)
        styleTab(btnProfileTab, true);
        styleTab(btnPasswordTab, false);

        // Add tab switching functionality
        btnProfileTab.addActionListener(e -> showCard("profile"));
        btnPasswordTab.addActionListener(e -> showCard("password"));

        tabs.add(btnProfileTab);
        tabs.add(btnPasswordTab);

        add(tabs, BorderLayout.NORTH);


        // Create main content area with card layout
        cardsPanel = new JPanel(new CardLayout());
        cardsPanel.setBackground(new Color(234, 242, 250));

        // Build individual panels
        buildProfilePanel();
        buildPasswordPanel();

        // Add panels to card layout
        cardsPanel.add(profilePanel, "profile");
        cardsPanel.add(passwordPanel, "password");

        add(cardsPanel, BorderLayout.CENTER);

        // Show profile panel by default
        showCard("profile");
    }


    /**
     * Applies styling to tab buttons based on selection state
     * Selected tabs have white background, unselected have green background
     * 
     * @param b The button to style
     * @param selected Whether the tab is currently selected
     */
    private void styleTab(JButton b, boolean selected) {
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Larger text for better visibility
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(100, 60));      // Taller buttons
        b.setBorder(BorderFactory.createEmptyBorder());

        // Set background color based on selection state
        if (selected) b.setBackground(Color.WHITE);
        else b.setBackground(new Color(199, 224, 199));
    }

    /**
     * Switches between different settings panels (profile/password)
     * Updates tab styling to reflect current selection
     * 
     * @param name Name of the card to show ("profile" or "password")
     */
    private void showCard(String name) {
        CardLayout cl = (CardLayout) cardsPanel.getLayout();
        cl.show(cardsPanel, name);

        // Update tab styling to reflect current selection
        styleTab(btnProfileTab, name.equals("profile"));
        styleTab(btnPasswordTab, name.equals("password"));
    }


    /**
     * Builds the profile editing panel
     * Allows users to update their personal information (name, phone, email)
     * Username is read-only for security reasons
     */
    private void buildProfilePanel() {
        profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(new Color(234, 242, 250));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(14, 14, 14, 14);
        c.anchor = GridBagConstraints.WEST;

        JLabel lName  = label("NAME:");
        JLabel lTel   = label("Tel:");
        JLabel lEmail = label("Email:");
        JLabel lUser  = label("Username:");

        JTextField tfName  = roundedField(user.getFullName());
        JTextField tfTel   = roundedField(user.getPhone());
        JTextField tfEmail = roundedField(user.getEmail());
        JTextField tfUser  = roundedField(user.getUsername());

        // Allow edit (admin or customer)
        tfName.setEditable(true);
        tfTel.setEditable(true);
        tfEmail.setEditable(true);
        tfUser.setEditable(false); // usually username should not change

        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(255, 217, 102));
        btnSave.setFocusPainted(false);

        // Add save functionality for profile updates
        btnSave.addActionListener(e -> {
            // Validate phone number (Rwandan format)
            String phone = tfTel.getText().trim();
            if (!phone.matches("07\\d{8}")) {
                JOptionPane.showMessageDialog(this, "Invalid phone number format! Use 07XXXXXXXX");
                tfTel.requestFocus();
                return;
            }
            
            // Validate email format
            String email = tfEmail.getText().trim();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format!");
                tfEmail.requestFocus();
                return;
            }
            
            // Validate name is not empty
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                tfName.requestFocus();
                return;
            }
            
            try {
                UserService userService = (UserService) Naming.lookup("rmi://localhost:3506/UserService");

                // Update user object with form data
                user.setFullName(name);
                user.setPhone(phone);
                user.setEmail(email);

                // Attempt to save changes to database
                boolean ok = userService.updateUserProfile(user);

                if (ok)
                    JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Failed to update profile.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating profile.");
            }
        });

        // Layout rows
        c.gridx = 0; c.gridy = 0; profilePanel.add(lName, c);
        c.gridx = 1; profilePanel.add(tfName, c);

        c.gridx = 0; c.gridy = 1; profilePanel.add(lTel, c);
        c.gridx = 1; profilePanel.add(tfTel, c);

        c.gridx = 0; c.gridy = 2; profilePanel.add(lEmail, c);
        c.gridx = 1; profilePanel.add(tfEmail, c);

        c.gridx = 0; c.gridy = 3; profilePanel.add(lUser, c);
        c.gridx = 1; profilePanel.add(tfUser, c);

        c.gridx = 1; c.gridy = 4; profilePanel.add(btnSave, c);
    }


    /**
     * Builds the password change panel
     * Provides secure password update functionality with validation
     * Requires old password verification and password strength validation
     */
    private void buildPasswordPanel() {
        passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(new Color(234, 242, 250));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(14, 14, 14, 14);
        c.anchor = GridBagConstraints.WEST;

        JLabel lOld     = label("Old Password:");
        JLabel lNew     = label("New Password:");
        JLabel lConfirm = label("Confirm New:");

        JPasswordField tfOld     = roundedPassword();
        JPasswordField tfNew     = roundedPassword();
        JPasswordField tfConfirm = roundedPassword();

        JButton btnSave = new JButton("Save Password");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(255, 217, 102));
        btnSave.setFocusPainted(false);

        // Add password change functionality with comprehensive validation
        btnSave.addActionListener(e -> {
            String oldP = new String(tfOld.getPassword());
            String newP = new String(tfNew.getPassword());
            String conf = new String(tfConfirm.getPassword());

            // Validate all fields are filled
            if (oldP.isEmpty() || newP.isEmpty() || conf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            
            // Verify password confirmation matches
            if (!newP.equals(conf)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.");
                return;
            }
            
            // Validate new password strength
            if (!PasswordValidator.isValidPassword(newP)) {
                JOptionPane.showMessageDialog(this, PasswordValidator.getPasswordRequirements(), "Invalid Password", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Attempt to change password with old password verification
            try {
                UserService userService = (UserService) Naming.lookup("rmi://localhost:3506/UserService");
                boolean ok = userService.changePassword(user.getId(), oldP, newP);
                
                if (ok)
                    JOptionPane.showMessageDialog(this, "Password changed successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Incorrect old password.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Server connection error: " + ex.getMessage());
            }


        });

        // layout
        c.gridx = 0; c.gridy = 0; passwordPanel.add(lOld, c);
        c.gridx = 1; passwordPanel.add(tfOld, c);

        c.gridx = 0; c.gridy = 1; passwordPanel.add(lNew, c);
        c.gridx = 1; passwordPanel.add(tfNew, c);

        c.gridx = 0; c.gridy = 2; passwordPanel.add(lConfirm, c);
        c.gridx = 1; passwordPanel.add(tfConfirm, c);

        c.gridx = 1; c.gridy = 3; passwordPanel.add(btnSave, c);
    }


    /**
     * Helper method to create styled labels
     * 
     * @param s Label text
     * @return Styled JLabel with consistent formatting
     */
    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return l;
    }

    /**
     * Helper method to create styled text fields with rounded borders
     * 
     * @param value Initial value for the text field
     * @return Styled JTextField with consistent formatting
     */
    private JTextField roundedField(String value) {
        JTextField tf = new JTextField(value, 18);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setPreferredSize(new Dimension(240, 38)); // Larger height for better usability
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }

    /**
     * Helper method to create styled password fields with rounded borders
     * 
     * @return Styled JPasswordField with consistent formatting
     */
    private JPasswordField roundedPassword() {
        JPasswordField tf = new JPasswordField(18);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setPreferredSize(new Dimension(240, 38));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }
    
    /**
     * Validates email format using regex pattern
     * 
     * @param email Email address to validate
     * @return true if email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
