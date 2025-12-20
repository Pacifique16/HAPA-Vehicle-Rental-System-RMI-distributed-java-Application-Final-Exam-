package view;

import service.BookingService;
import service.VehicleService;
import java.rmi.Naming;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AdminReportsPanel extends JPanel {

    private BookingService bookingService;
    private VehicleService vehicleService;
    private JTabbedPane tabs;
    private JTable tblActive, tblMostRented, tblAvailability, tblHistory;
    private boolean[] tabsLoaded = new boolean[4];

    public AdminReportsPanel() {
        initializeRMIServices();
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);
        buildTop();
        buildTabs();
    }
    
    private void initializeRMIServices() {
        try {
            bookingService = (BookingService) Naming.lookup("rmi://localhost:3506/BookingService");
            vehicleService = (VehicleService) Naming.lookup("rmi://localhost:3506/VehicleService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
        }
    }

    private void buildTop(){
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 109, 180));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnPdf = new JButton("Export to CSV");
        btnPdf.setBackground(new Color(255, 217, 102));
        btnPdf.setFocusPainted(false);
        btnPdf.addActionListener(e -> exportCurrentTab());
        
        buttonPanel.add(btnPdf);
        
        top.add(titleLabel, BorderLayout.WEST);
        top.add(buttonPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
    }

    private void buildTabs(){
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tblActive = new JTable(new DefaultTableModel(new String[]{"Customer","Vehicle","Start Date","End Date","Total Cost","Status"},0));
        tblActive.setRowHeight(25);
        tblActive.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblActive.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.addTab("Active Rentals", new JScrollPane(tblActive));

        tblMostRented = new JTable(new DefaultTableModel(new String[]{"Vehicle Model","Times Rented","Total Income"},0));
        tblMostRented.setRowHeight(25);
        tblMostRented.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblMostRented.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.addTab("Most Rented", new JScrollPane(tblMostRented));

        tblAvailability = new JTable(new DefaultTableModel(new String[]{"Vehicle Model","Category","Price/Day","Available Today"},0));
        tblAvailability.setRowHeight(25);
        tblAvailability.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblAvailability.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tblAvailability.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("Yes".equals(value)) {
                        c.setBackground(new Color(144, 238, 144));
                    } else {
                        c.setBackground(new Color(255, 182, 193));
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });
        
        tabs.addTab("Availability", new JScrollPane(tblAvailability));

        tblHistory = new JTable(new DefaultTableModel(new String[]{"Customer","Vehicle","Start Date","End Date","Total Cost","Status"},0));
        tblHistory.setRowHeight(25);
        tblHistory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblHistory.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.addTab("History", new JScrollPane(tblHistory));

        add(tabs, BorderLayout.CENTER);

        tabs.addChangeListener(e -> loadCurrentTabAsync());
        loadCurrentTabAsync();
    }

    private void loadCurrentTabAsync(){
        int idx = tabs.getSelectedIndex();
        if (idx < 0 || idx >= tabsLoaded.length) return;
        
        if (tabsLoaded[idx]) return;
        
        SwingWorker<Object, Void> worker = new SwingWorker<Object, Void>() {
            @Override
            protected Object doInBackground() throws Exception {
                if (bookingService == null) return null;
                
                if (idx == 0) return bookingService.getActiveRentalsReport();
                else if (idx == 1) return bookingService.getMostRentedVehicles();
                else if (idx == 2) return bookingService.getVehicleAvailabilityReport(new Date());
                else if (idx == 3) return bookingService.getBookingsHistoryReport();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    Object data = get();
                    if (data != null) {
                        if (idx == 0) populateActiveTable(data);
                        else if (idx == 1) populateMostRentedTable(data);
                        else if (idx == 2) populateAvailabilityTable(data);
                        else if (idx == 3) populateHistoryTable(data);
                        tabsLoaded[idx] = true;
                    }
                } catch (Exception ex){ ex.printStackTrace(); }
            }
        };
        worker.execute();
    }
    
    public void refreshAllTabs() {
        for (int i = 0; i < tabsLoaded.length; i++) {
            tabsLoaded[i] = false;
        }
        loadCurrentTabAsync();
    }
    
    private void populateActiveTable(Object data) {
        DefaultTableModel m = (DefaultTableModel) tblActive.getModel();
        m.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Object[]> list = (List<Object[]>) data;
        for (Object[] row : list) {
            m.addRow(row);
        }
    }
    
    private void populateMostRentedTable(Object data) {
        DefaultTableModel m = (DefaultTableModel) tblMostRented.getModel();
        m.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = (List<Object[]>) data;
        for (Object[] r : rows) {
            Object[] formatted = new Object[]{
                r[0],
                r[1],
                String.format("%,.0f RWF", (Double)r[2])
            };
            m.addRow(formatted);
        }
    }
    
    private void populateAvailabilityTable(Object data) {
        DefaultTableModel m = (DefaultTableModel) tblAvailability.getModel();
        m.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Object[]> availabilityData = (List<Object[]>) data;
        for (Object[] row : availabilityData) {
            m.addRow(row);
        }
        tblAvailability.repaint();
    }
    
    private void populateHistoryTable(Object data) {
        DefaultTableModel m = (DefaultTableModel) tblHistory.getModel();
        m.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = (List<Object[]>) data;
        for (Object[] row : rows) {
            m.addRow(row);
        }
    }

    private void exportCurrentTab(){
        int idx = tabs.getSelectedIndex();
        JTable current;
        String title;
        switch (idx) {
            case 0: current = tblActive; title = "Active Rentals Report"; break;
            case 1: current = tblMostRented; title = "Most Rented Vehicles Report"; break;
            case 2: current = tblAvailability; title = "Vehicle Availability Report"; break;
            default: current = tblHistory; title = "Bookings History Report"; break;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save CSV Report");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        int ret = fc.showSaveDialog(this);
        if (ret!=JFileChooser.APPROVE_OPTION) return;
        String path = fc.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) path += ".csv";
        try {
            exportTableToCsv(current, title, path);
            JOptionPane.showMessageDialog(this, "Report exported successfully to: " + path);
        } catch (Exception ex){ 
            ex.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    private void exportTableToCsv(JTable table, String title, String filePath) throws Exception {
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            writer.append("HAPA Vehicle Rental System - " + title + "\n");
            writer.append("Generated on: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n\n");
            
            for (int i = 0; i < table.getColumnCount(); i++) {
                writer.append(table.getColumnName(i));
                if (i < table.getColumnCount() - 1) writer.append(",");
            }
            writer.append("\n");
            
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    String cellValue = value != null ? value.toString().replace(",", ";") : "";
                    writer.append(cellValue);
                    if (col < table.getColumnCount() - 1) writer.append(",");
                }
                writer.append("\n");
            }
        }
    }
}