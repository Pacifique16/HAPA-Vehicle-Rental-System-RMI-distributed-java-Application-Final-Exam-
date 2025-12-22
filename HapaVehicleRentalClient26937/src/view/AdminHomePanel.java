/*
 * HAPA Vehicle Rental System - Admin Home Dashboard Panel
 * Provides overview dashboard for administrators with key metrics and recent activity
 * Features summary cards for statistics and today's bookings table
 * Optimized for performance with RMI service calls
 */
package view;

/**
 * AdminHomePanel - Main dashboard interface for administrators
 * Displays key system metrics and today's booking activity
 * Features optimized analytics loading and interactive booking details
 *
 * @author Pacifique Harerimana
 */

import service.UserService;
import service.VehicleService;
import service.BookingService;
import java.rmi.Naming;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Admin dashboard panel with summary cards and recent bookings table
 * Provides quick overview of system status and today's activity
 */
public class AdminHomePanel extends JPanel {

    // RMI service instances
    private UserService userService;
    private VehicleService vehicleService;
    private BookingService bookingService;

    // UI components for summary cards
    private JLabel lblVehicles;        // Total vehicles count
    private JLabel lblUsers;           // Total users count
    private JLabel lblRentals;         // Total rentals count
    private JLabel lblAvailable;       // Available vehicles today
    
    // Table for displaying today's bookings
    private JTable recentBookingsTable;

    /**
     * Constructor - Initializes the admin home dashboard
     * Sets up summary cards and recent bookings table with data loading
     */
    public AdminHomePanel() {
        initializeRMIServices();
        setLayout(new BorderLayout(12,12));
        setBackground(Color.WHITE);
        buildTopCards();        // Create summary statistics cards
        buildRecent();          // Create recent bookings table
        loadAnalytics();        // Load statistics data
        loadRecentBookings();   // Load today's bookings
    }
    
    private void initializeRMIServices() {
        try {
            userService = (UserService) Naming.lookup("rmi://localhost:3506/UserService");
            vehicleService = (VehicleService) Naming.lookup("rmi://localhost:3506/VehicleService");
            bookingService = (BookingService) Naming.lookup("rmi://localhost:3506/BookingService");
        } catch (Exception e) {
            System.err.println("RMI connection error: " + e.getMessage());
        }
    }

    /**
     * Builds the top summary cards section
     * Creates four cards showing key system statistics
     */
    private void buildTopCards(){
        JPanel cards = new JPanel(new GridLayout(1,4,12,12));
        cards.setOpaque(false);

        // Create summary cards with initial zero values
        lblVehicles = makeCard("Total Vehicles", "0");
        lblUsers = makeCard("Total Users", "0");
        lblRentals = makeCard("Total Rentals", "0");
        lblAvailable = makeCard("Vehicles Available Today", "0");

        // Wrap cards with borders and add to panel
        cards.add(wrapCard(lblVehicles));
        cards.add(wrapCard(lblUsers));
        cards.add(wrapCard(lblRentals));
        cards.add(wrapCard(lblAvailable));

        add(cards, BorderLayout.NORTH);
    }

    /**
     * Wraps a card label with proper styling and borders
     * 
     * @param center The label to wrap
     * @return Styled panel containing the card
     */
    private JPanel wrapCard(JLabel center){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230)),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    /**
     * Creates a styled card label with title and value
     * 
     * @param title Card title text
     * @param value Card value text
     * @return Styled JLabel with HTML formatting
     */
    private JLabel makeCard(String title, String value){
        JLabel l = new JLabel("<html><div style='text-align:center'><div style='font-size:18px;color:#222;'>"+value+"</div><div style='font-size:12px;color:#666;'>"+title+"</div></div></html>");
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    /**
     * Builds the recent bookings table section
     * Creates table showing today's bookings with interactive features
     */
    private void buildRecent(){
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        
        // Section header
        JLabel h = new JLabel("Today's Bookings");
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(h, BorderLayout.NORTH);

        // Create bookings table
        String[] columns = {"Customer", "Vehicle", "Pick-up", "Drop-off", "Status", "Total"};
        recentBookingsTable = new JTable(new javax.swing.table.DefaultTableModel(columns, 0));
        recentBookingsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recentBookingsTable.setRowHeight(25);
        recentBookingsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // Add double-click listener for booking details
        recentBookingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showBookingDetails();
                }
            }
        });
        
        // Apply custom renderer for status color coding
        recentBookingsTable.setDefaultRenderer(Object.class, new StatusTableRenderer());
        
        JScrollPane sp = new JScrollPane(recentBookingsTable);
        p.add(sp, BorderLayout.CENTER);

        add(p, BorderLayout.CENTER);
    }

    /**
     * Loads analytics data for summary cards
     * Uses RMI services to get statistics
     */
    private void loadAnalytics(){
        try {
            if (bookingService != null) {
                // Get statistics from RMI service using optimized query
                int[] stats = bookingService.getDashboardStats();
                
                // Update card labels with actual data
                lblVehicles.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>" + stats[0] + "</div><div style='font-size:12px;color:#666;'>Total Vehicles</div></div></html>");
                lblUsers.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>" + stats[1] + "</div><div style='font-size:12px;color:#666;'>Total Users</div></div></html>");
                lblRentals.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>" + stats[2] + "</div><div style='font-size:12px;color:#666;'>Total Rentals</div></div></html>");
                lblAvailable.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>" + stats[3] + "</div><div style='font-size:12px;color:#666;'>Vehicles Available Today</div></div></html>");
            } else {
                System.out.println("BookingService is null, using sample data");
                // Show sample data if RMI not available
                lblVehicles.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>27</div><div style='font-size:12px;color:#666;'>Total Vehicles</div></div></html>");
                lblUsers.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>14</div><div style='font-size:12px;color:#666;'>Total Users</div></div></html>");
                lblRentals.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>87</div><div style='font-size:12px;color:#666;'>Total Rentals</div></div></html>");
                lblAvailable.setText("<html><div style='text-align:center'><div style='font-size:20px;color:#222;'>24</div><div style='font-size:12px;color:#666;'>Vehicles Available Today</div></div></html>");
            }
        } catch (Exception ex){
            System.err.println("Error loading analytics: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void loadRecentBookings() {
        try {
            DefaultTableModel model = (DefaultTableModel) recentBookingsTable.getModel();
            model.setRowCount(0); // Clear existing data
            
            if (bookingService != null) {
                // Get today's bookings with customer and vehicle details in one call
                java.util.List<String[]> bookingDetails = bookingService.getTodaysBookingsWithDetails();
                
                for (String[] row : bookingDetails) {
                    model.addRow(row);
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Shows detailed information for selected booking
     * Triggered by double-clicking on a table row
     */
    private void showBookingDetails() {
        int row = recentBookingsTable.getSelectedRow();
        if (row >= 0) {
            // Extract booking information from selected row
            String customer = (String) recentBookingsTable.getValueAt(row, 0);
            String vehicle = (String) recentBookingsTable.getValueAt(row, 1);
            String pickupDate = recentBookingsTable.getValueAt(row, 2).toString();
            String dropoffDate = recentBookingsTable.getValueAt(row, 3).toString();
            String status = (String) recentBookingsTable.getValueAt(row, 4);
            String total = (String) recentBookingsTable.getValueAt(row, 5);
            
            // Format booking details for display
            String details = String.format(
                "Booking Details:\n\n" +
                "Customer: %s\n" +
                "Vehicle: %s\n" +
                "Pick-up Date: %s\n" +
                "Drop-off Date: %s\n" +
                "Status: %s\n" +
                "Total: %s",
                customer, vehicle, pickupDate, dropoffDate, status, total
            );
            
            // Show details in dialog
            JOptionPane.showMessageDialog(this, details, "Booking Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Refreshes dashboard data - loads latest statistics and bookings
     * Can be called to update the dashboard with current data
     */
    public void refreshDashboard() {
        loadAnalytics();
        loadRecentBookings();
    }
    
    /**
     * Custom table cell renderer for status color coding
     * Applies different background colors based on booking status
     */
    private class StatusTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Apply color coding to status column (index 4)
            if (column == 4 && value != null) {
                String status = value.toString();
                if (!isSelected) {
                    // Set background color based on booking status
                    switch (status.toUpperCase()) {
                        case "APPROVED":
                            c.setBackground(new Color(220, 255, 220)); // Light green
                            break;
                        case "PENDING":
                            c.setBackground(new Color(255, 255, 220)); // Light yellow
                            break;
                        case "REJECTED":
                        case "CANCELLED":
                            c.setBackground(new Color(255, 220, 220)); // Light red
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
            } else if (!isSelected) {
                c.setBackground(Color.WHITE);
            }
            
            return c;
        }
    }
}