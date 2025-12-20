package view;

import service.BookingService;
import java.rmi.Naming;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookingsApprovalPanel extends JPanel {

    private BookingService bookingService;
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JComboBox<String> statusFilter;

    public BookingsApprovalPanel() {
        initializeRMIService();
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);
        buildTop();
        buildTable();
        loadAll();
    }
    
    private void initializeRMIService() {
        try {
            bookingService = (BookingService) Naming.lookup("rmi://localhost:3506/BookingService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
        }
    }

    private void buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        tfSearch = new JTextField(20);
        tfSearch.addActionListener(e -> doSearch());

        statusFilter = new JComboBox<>(new String[]{"All Status", "PENDING", "APPROVED", "REJECTED"});
        statusFilter.addActionListener(e -> doAdvancedSearch());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(tfSearch);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(new JLabel("Status:"));
        searchPanel.add(statusFilter);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        
        JButton bRefresh = new JButton("Refresh");
        JButton bExport = new JButton("Export CSV");
        JButton bBulkApprove = new JButton("Bulk Approve");
        JButton bBulkReject = new JButton("Bulk Reject");
        
        bRefresh.addActionListener(e -> loadAll());
        bExport.addActionListener(e -> exportToCSV());
        bBulkApprove.addActionListener(e -> bulkApprove());
        bBulkReject.addActionListener(e -> bulkReject());
        
        actionPanel.add(bExport);
        actionPanel.add(bBulkApprove);
        actionPanel.add(bBulkReject);
        actionPanel.add(bRefresh);

        top.add(searchPanel, BorderLayout.WEST);
        top.add(actionPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
    }

    private void buildTable() {
        String[] cols = new String[]{"ID","Customer","Phone","Vehicle","Plate","Start Date","End Date","Total Cost","Status"};
        model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;}};
        table = new JTable(model);
        table.removeColumn(table.getColumnModel().getColumn(0)); // hide ID
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPopupMenu pm = new JPopupMenu();
        JMenuItem miApprove = new JMenuItem("Approve");
        JMenuItem miReject = new JMenuItem("Reject");
        miApprove.addActionListener(e -> doApprove());
        miReject.addActionListener(e -> doReject());
        pm.add(miApprove); 
        pm.add(miReject);
        table.setComponentPopupMenu(pm);
    }

    private void loadAll(){
        model.setRowCount(0);
        try {
            if (bookingService != null) {
                List<Object[]> list = bookingService.getPendingBookingsReport();
                for (Object[] row : list) {
                    model.addRow(row);
                }
            }
        } catch(Exception ex){ 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage());
        }
    }

    private void doSearch(){
        String q = tfSearch.getText().trim().toLowerCase();
        model.setRowCount(0);
        try {
            if (bookingService != null) {
                List<Object[]> allBookings = bookingService.getPendingBookingsReport();
                for (Object[] row : allBookings) {
                    if (q.isEmpty() || matchesSearchRow(row, q)) {
                        model.addRow(row);
                    }
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    private boolean matchesSearchRow(Object[] row, String query) {
        for (int i = 1; i < row.length; i++) {
            if (row[i] != null && row[i].toString().toLowerCase().contains(query)) {
                return true;
            }
        }
        return false;
    }

    private void doApprove(){
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to approve");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to approve this booking?", 
            "Confirm Approval", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        int modelIndex = table.convertRowIndexToModel(sel);
        int id = Integer.parseInt(model.getValueAt(modelIndex,0).toString());
        
        try {
            if (bookingService != null) {
                boolean ok = bookingService.approveBooking(id);
                if (ok) { 
                    JOptionPane.showMessageDialog(this, "Booking approved successfully!"); 
                    loadAll();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to approve booking.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void doReject(){
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to reject");
            return;
        }
        
        String reason = JOptionPane.showInputDialog(this, "Enter rejection reason:");
        if (reason == null) return;
        
        if (reason.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rejection reason is required!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reject this booking?", 
            "Confirm Rejection", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        int modelIndex = table.convertRowIndexToModel(sel);
        int id = Integer.parseInt(model.getValueAt(modelIndex,0).toString());
        
        try {
            if (bookingService != null) {
                boolean ok = bookingService.rejectBookingWithReason(id, reason.trim());
                if (ok) { 
                    JOptionPane.showMessageDialog(this, "Booking rejected successfully!"); 
                    loadAll();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reject booking.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void doAdvancedSearch() {
        String selectedStatus = (String) statusFilter.getSelectedItem();
        String searchText = tfSearch.getText().trim().toLowerCase();
        
        model.setRowCount(0);
        try {
            if (bookingService != null) {
                List<Object[]> list = bookingService.getPendingBookingsReport();
                
                for (Object[] row : list) {
                    if (!searchText.isEmpty() && !matchesSearchRow(row, searchText)) {
                        continue;
                    }
                    
                    String status = row[8].toString();
                    if (!selectedStatus.equals("All Status") && !status.equals(selectedStatus)) {
                        continue;
                    }
                    
                    model.addRow(row);
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    private void exportToCSV() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save CSV Report");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
            
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".csv")) path += ".csv";
                
                try (java.io.FileWriter writer = new java.io.FileWriter(path)) {
                    writer.append("HAPA Vehicle Rental System - Bookings Approval Report\n");
                    writer.append("Generated on: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n\n");
                    writer.append("Customer,Phone,Vehicle,Plate,Start Date,End Date,Total Cost,Status\n");
                    
                    for (int i = 0; i < model.getRowCount(); i++) {
                        for (int j = 1; j < model.getColumnCount(); j++) {
                            String cellValue = model.getValueAt(i, j) != null ? 
                                model.getValueAt(i, j).toString().replace(",", ";") : "";
                            writer.append(cellValue);
                            if (j < model.getColumnCount() - 1) writer.append(",");
                        }
                        writer.append("\n");
                    }
                }
                JOptionPane.showMessageDialog(this, "Report exported successfully to: " + path);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void bulkApprove() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select bookings to approve");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Approve " + rows.length + " bookings?", "Confirm Bulk Approval", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int approved = 0;
            for (int row : rows) {
                int modelIndex = table.convertRowIndexToModel(row);
                int id = Integer.parseInt(model.getValueAt(modelIndex, 0).toString());
                try {
                    if (bookingService != null && bookingService.approveBooking(id)) {
                        approved++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Approved " + approved + " bookings");
            loadAll();
        }
    }
    
    private void bulkReject() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select bookings to reject");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Reject " + rows.length + " bookings?", "Confirm Bulk Rejection", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int rejected = 0;
            for (int row : rows) {
                int modelIndex = table.convertRowIndexToModel(row);
                int id = Integer.parseInt(model.getValueAt(modelIndex, 0).toString());
                try {
                    if (bookingService != null && bookingService.rejectBooking(id)) {
                        rejected++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Rejected " + rejected + " bookings");
            loadAll();
        }
    }
    
    private class AlternatingRowRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(248, 248, 248));
                }
                
                if (column == 8 && value != null) { // Status column
                    String status = value.toString();
                    switch (status) {
                        case "PENDING":
                            c.setBackground(new Color(255, 255, 220));
                            break;
                        case "APPROVED":
                            c.setBackground(new Color(220, 255, 220));
                            break;
                        case "REJECTED":
                            c.setBackground(new Color(255, 220, 220));
                            break;
                    }
                }
            }
            
            return c;
        }
    }
}