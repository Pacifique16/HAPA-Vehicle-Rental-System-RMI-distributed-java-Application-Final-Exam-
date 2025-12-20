package view;

import model.User;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AdminDashboard extends JFrame {
    public AdminDashboard(User user) {
        setTitle("Admin Dashboard - " + user.getFullName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Admin Dashboard - RMI Transformation in Progress"));
        setLocationRelativeTo(null);
    }
}