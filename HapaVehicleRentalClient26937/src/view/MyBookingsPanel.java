/*
 * HAPA Vehicle Rental System - My Bookings Panel
 * Customer interface for browsing and managing their vehicle bookings
 * Features advanced filtering, table view, booking preview, and pagination
 * Adapted from original design to use RMI services
 */
package view;

import service.BookingService;
import model.User;
import model.Booking;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyBookingsPanel extends JPanel {

    private final User user;
    private BookingService bookingService;

    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblImage, lblModel, lblDates, lblDays, lblTotal, lblStatus;
    private JButton btnCancelBooking, btnReopenBooking;

    // Filters
    private JTextField tfModel;
    private JComboBox<String> cbStatus;
    private JSpinner spFrom, spTo;
    private JButton bThisMonth, bPast, bUpcoming, bClearDates;
    private JButton btnExport;

    // Pagination
    private final int pageSize = 5;
    private int currentPage = 1;
    private int totalPages = 1;
    private JLabel lblPageInfo;
    private JPanel paginationPanel;
    
    // Store current search results for row selection
    private List<Booking> currentSearchResults = new java.util.ArrayList<>();
    private java.util.Map<Integer, model.Vehicle> vehicleMap = new java.util.HashMap<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public MyBookingsPanel(User user) {
        this.user = user;
        initComponents();
        loadSampleData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Top area: filter rows
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Search field
        JPanel pSearch = new JPanel(new BorderLayout(6, 0));
        pSearch.setBackground(Color.WHITE);
        JLabel searchIcon = new JLabel("Search:");
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        tfModel = new JTextField();
        tfModel.setColumns(14);
        styleRounded(tfModel);
        tfModel.setToolTipText("Search by vehicle model, plate, status, dates...");
        pSearch.add(searchIcon, BorderLayout.WEST);
        pSearch.add(tfModel, BorderLayout.CENTER);

        // Status combo
        cbStatus = new JComboBox<>(new String[]{"All status", "PENDING", "APPROVED", "REJECTED", "CANCELLED", "EXPIRED"});
        styleRounded(cbStatus);
        cbStatus.setPreferredSize(new Dimension(140, cbStatus.getPreferredSize().height));

        // Date spinners
        JPanel pFrom = new JPanel(new BorderLayout(6, 0));
        pFrom.setBackground(Color.WHITE);
        JLabel calFrom = new JLabel("Start:");
        calFrom.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        spFrom = new JSpinner(new SpinnerDateModel());
        spFrom.setEditor(new JSpinner.DateEditor(spFrom, "yyyy-MM-dd"));
        spFrom.setPreferredSize(new Dimension(120, spFrom.getPreferredSize().height));
        JPanel wrapFrom = new JPanel(new BorderLayout());
        wrapFrom.setBackground(Color.WHITE);
        styleRounded(wrapFrom);
        wrapFrom.add(spFrom);
        pFrom.add(calFrom, BorderLayout.WEST);
        pFrom.add(wrapFrom, BorderLayout.CENTER);

        JPanel pTo = new JPanel(new BorderLayout(6, 0));
        pTo.setBackground(Color.WHITE);
        JLabel calTo = new JLabel("End:");
        calTo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        spTo = new JSpinner(new SpinnerDateModel());
        spTo.setEditor(new JSpinner.DateEditor(spTo, "yyyy-MM-dd"));
        spTo.setPreferredSize(new Dimension(120, spTo.getPreferredSize().height));
        JPanel wrapTo = new JPanel(new BorderLayout());
        wrapTo.setBackground(Color.WHITE);
        styleRounded(wrapTo);
        wrapTo.add(spTo);
        pTo.add(calTo, BorderLayout.WEST);
        pTo.add(wrapTo, BorderLayout.CENTER);

        // Place controls into grid
        c.gridx = 0; c.gridy = 0; c.weightx = 0.45;
        top.add(pSearch, c);
        c.gridx = 1; c.gridy = 0; c.weightx = 0.18;
        top.add(cbStatus, c);
        c.gridx = 2; c.gridy = 0; c.weightx = 0.18;
        top.add(pFrom, c);
        c.gridx = 3; c.gridy = 0; c.weightx = 0.18;
        top.add(pTo, c);

        // Quick action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setBackground(Color.WHITE);

        bThisMonth = new JButton("This month");
        bPast = new JButton("Past");
        bUpcoming = new JButton("Upcoming");
        bClearDates = new JButton("Clear dates");
        JButton btnExportPdf = new JButton("Export PDF");
        JButton btnExportCsv = new JButton("Export CSV");

        for (JButton bb : new JButton[]{bThisMonth, bPast, bUpcoming, bClearDates}) {
            bb.setFocusPainted(false);
            bb.setBorder(new RoundedBorder(12, new Color(200, 200, 200)));
            bb.setBackground(new Color(250, 250, 250));
            bb.setPreferredSize(new Dimension(110, 28));
        }
        btnExportPdf.setFocusPainted(false);
        btnExportPdf.setBackground(new Color(255, 217, 102));
        btnExportPdf.setPreferredSize(new Dimension(110, 28));
        
        btnExportCsv.setFocusPainted(false);
        btnExportCsv.setBackground(new Color(220, 220, 220));
        btnExportCsv.setPreferredSize(new Dimension(110, 28));

        actions.add(bThisMonth);
        actions.add(bPast);
        actions.add(bUpcoming);
        actions.add(bClearDates);
        actions.add(Box.createHorizontalStrut(12));
        actions.add(btnExportPdf);
        actions.add(btnExportCsv);

        c.gridx = 0; c.gridy = 1; c.gridwidth = 4; c.weightx = 1;
        top.add(actions, c);

        add(top, BorderLayout.NORTH);

        // Center: Split pane with table and preview
        String[] cols = new String[]{"ID", "Vehicle", "Start", "End", "Days", "Total", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.removeColumn(table.getColumnModel().getColumn(0)); // hide ID column
        JScrollPane spTable = new JScrollPane(table);

        // Preview panel (right)
        JPanel preview = new JPanel();
        preview.setLayout(new BoxLayout(preview, BoxLayout.Y_AXIS));
        preview.setBackground(Color.WHITE);
        preview.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        preview.setPreferredSize(new Dimension(360, 0));

        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(200, 130));
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblModel = new JLabel("Select a booking");
        lblModel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblModel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDates = new JLabel("");
        lblDates.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDays = new JLabel("");
        lblDays.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotal = new JLabel("");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(34, 109, 180));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatus = new JLabel("");
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCancelBooking = new JButton("CANCEL BOOKING");
        btnCancelBooking.setBackground(new Color(255, 217, 102));
        btnCancelBooking.setFocusPainted(false);
        btnCancelBooking.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelBooking.setEnabled(false);

        btnReopenBooking = new JButton("REOPEN BOOKING");
        btnReopenBooking.setBackground(new Color(240, 240, 240));
        btnReopenBooking.setFocusPainted(false);
        btnReopenBooking.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReopenBooking.setEnabled(false);

        preview.add(lblImage);
        preview.add(Box.createVerticalStrut(8));
        preview.add(lblModel);
        preview.add(Box.createVerticalStrut(6));
        preview.add(lblDates);
        preview.add(Box.createVerticalStrut(4));
        preview.add(lblDays);
        preview.add(Box.createVerticalStrut(8));
        preview.add(lblTotal);
        preview.add(Box.createVerticalStrut(8));
        preview.add(lblStatus);
        preview.add(Box.createVerticalStrut(12));
        preview.add(btnCancelBooking);
        preview.add(Box.createVerticalStrut(8));
        preview.add(btnReopenBooking);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spTable, preview);
        split.setResizeWeight(0.66);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // Bottom: pagination
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        paginationPanel.setBackground(Color.WHITE);
        lblPageInfo = new JLabel("Page 1 of 1 (4 bookings)");
        bottom.add(paginationPanel, BorderLayout.CENTER);
        bottom.add(lblPageInfo, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);



        // Listeners for filters
        tfModel.addActionListener(e -> goFirstAndReload());
        cbStatus.addActionListener(e -> goFirstAndReload());
        spFrom.addChangeListener(e -> goFirstAndReload());
        spTo.addChangeListener(e -> goFirstAndReload());

        bThisMonth.addActionListener(e -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            spFrom.setValue(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            spTo.setValue(cal.getTime());
            goFirstAndReload();
        });

        bPast.addActionListener(e -> {
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DAY_OF_MONTH, -1);
            spTo.setValue(today.getTime());
            Calendar past = Calendar.getInstance();
            past.set(2000, Calendar.JANUARY, 1);
            spFrom.setValue(past.getTime());
            goFirstAndReload();
        });

        bUpcoming.addActionListener(e -> {
            Calendar today = Calendar.getInstance();
            spFrom.setValue(today.getTime());
            Calendar far = Calendar.getInstance();
            far.add(Calendar.YEAR, 30);
            spTo.setValue(far.getTime());
            goFirstAndReload();
        });

        bClearDates.addActionListener(e -> {
            Calendar s = Calendar.getInstance();
            s.set(2000, Calendar.JANUARY, 1);
            spFrom.setValue(s.getTime());
            Calendar eCal = Calendar.getInstance();
            eCal.set(2100, Calendar.JANUARY, 1);
            spTo.setValue(eCal.getTime());
            goFirstAndReload();
        });

        btnExportPdf.addActionListener(e -> exportPdfCurrentFiltered());
        btnExportCsv.addActionListener(e -> exportCsvCurrentFiltered());
        btnCancelBooking.addActionListener(e -> cancelSelectedBooking());
        btnReopenBooking.addActionListener(e -> reopenSelectedBooking());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onRowSelected();
        });

        // Set default dates
        Calendar defFrom = Calendar.getInstance();
        defFrom.set(2024, Calendar.JANUARY, 1);
        spFrom.setValue(defFrom.getTime());
        Calendar defTo = Calendar.getInstance();
        defTo.set(2050, Calendar.DECEMBER, 31);
        spTo.setValue(defTo.getTime());
    }

    private void styleRounded(JComponent comp) {
        comp.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, new Color(200,200,200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        comp.setBackground(Color.WHITE);
    }

    private void loadSampleData() {
        applyFiltersAndLoad();
    }
    
    private void applyFiltersAndLoad() {
        try {
            // Connect to RMI services
            bookingService = (BookingService) Naming.lookup("rmi://localhost:3506/BookingService");
            service.VehicleService vehicleService = (service.VehicleService) Naming.lookup("rmi://localhost:3506/VehicleService");
            
            // Load vehicle map for details display
            List<model.Vehicle> vehicles = vehicleService.getAllVehicles();
            vehicleMap.clear();
            for (model.Vehicle v : vehicles) {
                vehicleMap.put(v.getId(), v);
            }
            
            // Get all bookings for user
            List<Booking> allBookings = bookingService.getBookingsByCustomerId(user.getId());
            
            // Apply filters
            String searchQuery = tfModel.getText().trim().toLowerCase();
            String statusFilter = (String) cbStatus.getSelectedItem();
            if (statusFilter != null && (statusFilter.isEmpty() || statusFilter.equals("All status"))) statusFilter = null;
            Date fromDate = (Date) spFrom.getValue();
            Date toDate = (Date) spTo.getValue();
            
            currentSearchResults = new java.util.ArrayList<>();
            for (Booking booking : allBookings) {
                // Apply status filter
                if (statusFilter != null && !statusFilter.equals(booking.getStatus())) continue;
                
                // Apply date filter
                if (booking.getStartDate().before(fromDate) || booking.getEndDate().after(toDate)) continue;
                
                // Apply search filter
                if (!searchQuery.isEmpty() && !matchesSearch(booking, searchQuery)) continue;
                
                currentSearchResults.add(booking);
            }
            
            // Calculate pagination
            int totalRows = currentSearchResults.size();
            totalPages = Math.max(1, (int)Math.ceil(totalRows / (double) pageSize));
            if (currentPage > totalPages) currentPage = totalPages;

            int offset = (currentPage - 1) * pageSize;
            List<Booking> page = new java.util.ArrayList<>();
            for (int i = offset; i < Math.min(offset + pageSize, currentSearchResults.size()); i++) {
                page.add(currentSearchResults.get(i));
            }

            // Update table
            tableModel.setRowCount(0);
            if (page.isEmpty()) {
                Object[] emptyRow = {0, "No bookings found", "", "", "", "", ""};
                tableModel.addRow(emptyRow);
            } else {
                for (Booking booking : page) {
                    model.Vehicle vehicle = vehicleMap.get(booking.getVehicleId());
                    String vehicleName = vehicle != null ? vehicle.getModel() : "Vehicle ID: " + booking.getVehicleId();
                    long days = Math.max(1, (booking.getEndDate().getTime() - booking.getStartDate().getTime()) / (1000L * 60 * 60 * 24) + 1);
                    
                    Object[] row = {
                        booking.getId(),
                        vehicleName,
                        sdf.format(booking.getStartDate()),
                        sdf.format(booking.getEndDate()),
                        days,
                        String.format("%,.0f", booking.getTotalCost()),
                        booking.getStatus()
                    };
                    tableModel.addRow(row);
                }
            }
            
            rebuildPaginationControls();
            lblPageInfo.setText(String.format("Page %d of %d (%d bookings)", currentPage, totalPages, totalRows));

            if (tableModel.getRowCount() > 0 && !page.isEmpty()) {
                table.setRowSelectionInterval(0, 0);
                onRowSelected();
            } else {
                clearPreview();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void goFirstAndReload() {
        currentPage = 1;
        applyFiltersAndLoad();
    }
    
    private boolean matchesSearch(Booking booking, String query) {
        model.Vehicle vehicle = vehicleMap.get(booking.getVehicleId());
        if (vehicle != null) {
            if (vehicle.getModel().toLowerCase().contains(query) ||
                vehicle.getPlateNumber().toLowerCase().contains(query) ||
                vehicle.getCategory().toLowerCase().contains(query)) {
                return true;
            }
        }
        return booking.getStatus().toLowerCase().contains(query) ||
               sdf.format(booking.getStartDate()).contains(query) ||
               sdf.format(booking.getEndDate()).contains(query) ||
               String.valueOf((int)booking.getTotalCost()).contains(query);
    }

    private void onRowSelected() {
        int sel = table.getSelectedRow();
        if (sel < 0) { clearPreview(); return; }
        
        int modelIndex = table.convertRowIndexToModel(sel);
        int bookingId = Integer.parseInt(tableModel.getValueAt(modelIndex, 0).toString());
        
        // Find the booking in current results
        Booking booking = null;
        for (Booking b : currentSearchResults) {
            if (b.getId() == bookingId) {
                booking = b;
                break;
            }
        }
        
        if (booking == null) { clearPreview(); return; }
        
        model.Vehicle vehicle = vehicleMap.get(booking.getVehicleId());
        if (vehicle == null) { clearPreview(); return; }
        
        lblModel.setText(vehicle.getModel());
        lblDates.setText("From: " + sdf.format(booking.getStartDate()) + "   To: " + sdf.format(booking.getEndDate()));
        long days = Math.max(1L, (booking.getEndDate().getTime() - booking.getStartDate().getTime())/(1000L*60*60*24) + 1);
        lblDays.setText("Days: " + days);
        lblTotal.setText("Total: " + String.format("%,.0f RWF", booking.getTotalCost()));
        
        // Show rejection reason if booking is rejected (handle both old and new status formats)
        if ("REJECTED".equals(booking.getStatus()) || "Rejected".equals(booking.getStatus())) {
            System.out.println("DEBUG: Booking " + booking.getId() + " is REJECTED/Rejected");
            System.out.println("DEBUG: Rejection reason: '" + booking.getRejectionReason() + "'");
            System.out.println("DEBUG: Rejection reason is null: " + (booking.getRejectionReason() == null));
            System.out.println("DEBUG: Rejection reason is empty: " + (booking.getRejectionReason() != null && booking.getRejectionReason().trim().isEmpty()));
            
            if (booking.getRejectionReason() != null && !booking.getRejectionReason().trim().isEmpty()) {
                lblStatus.setText("<html><b>Status: REJECTED</b><br><font color='red'>Reason: " + booking.getRejectionReason() + "</font></html>");
            } else {
                lblStatus.setText("<html><b>Status: REJECTED</b><br><font color='gray'>(No reason provided)</font></html>");
            }
        } else {
            lblStatus.setText("Status: " + booking.getStatus());
        }

        // Set vehicle image
        try {
            ImageIcon ic = new ImageIcon(vehicle.getImagePath());
            Image img = ic.getImage().getScaledInstance(200, 130, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
            lblImage.setText("");
        } catch (Exception ex) {
            lblImage.setIcon(null);
            lblImage.setText("🚗 " + vehicle.getModel());
            lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        }

        boolean canCancel = "PENDING".equalsIgnoreCase(booking.getStatus());
        boolean canReopen = "CANCELLED".equalsIgnoreCase(booking.getStatus());
        btnCancelBooking.setEnabled(canCancel);
        btnReopenBooking.setEnabled(canReopen);
    }
    
    private void clearPreview() {
        lblModel.setText("No booking selected");
        lblDates.setText("");
        lblDays.setText("");
        lblTotal.setText("");
        lblStatus.setText("");
        lblImage.setIcon(null);
        lblImage.setText("");
        btnCancelBooking.setEnabled(false);
        btnReopenBooking.setEnabled(false);
    }

    private void rebuildPaginationControls() {
        paginationPanel.removeAll();

        JButton first = new JButton("<< First");
        JButton prev = new JButton("< Previous");
        JButton next = new JButton("Next >");
        JButton last = new JButton("Last >>");

        first.setEnabled(currentPage > 1);
        prev.setEnabled(currentPage > 1);
        next.setEnabled(currentPage < totalPages);
        last.setEnabled(currentPage < totalPages);

        first.addActionListener(e -> { currentPage = 1; applyFiltersAndLoad(); });
        prev.addActionListener(e -> { if (currentPage > 1) currentPage--; applyFiltersAndLoad(); });
        next.addActionListener(e -> { if (currentPage < totalPages) currentPage++; applyFiltersAndLoad(); });
        last.addActionListener(e -> { currentPage = totalPages; applyFiltersAndLoad(); });

        paginationPanel.add(first);
        paginationPanel.add(prev);

        int visible = 7;
        int start = Math.max(1, currentPage - visible/2);
        int end = Math.min(totalPages, start + visible - 1);
        if (end - start + 1 < visible) start = Math.max(1, end - visible + 1);

        for (int p = start; p <= end; p++) {
            final int pi = p;
            JButton b = new JButton(String.valueOf(p));
            if (p == currentPage) b.setEnabled(false);
            b.addActionListener(e -> { currentPage = pi; applyFiltersAndLoad(); });
            paginationPanel.add(b);
        }

        paginationPanel.add(next);
        paginationPanel.add(last);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }
    
    private void cancelSelectedBooking() {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        int modelIndex = table.convertRowIndexToModel(sel);
        int bookingId = Integer.parseInt(tableModel.getValueAt(modelIndex, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = bookingService.cancelBooking(bookingId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Booking cancelled.");
                applyFiltersAndLoad();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cancelling booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reopenSelectedBooking() {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        int modelIndex = table.convertRowIndexToModel(sel);
        int bookingId = Integer.parseInt(tableModel.getValueAt(modelIndex, 0).toString());

        // Find the booking
        Booking booking = null;
        for (Booking b : currentSearchResults) {
            if (b.getId() == bookingId) {
                booking = b;
                break;
            }
        }
        if (booking == null) return;

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Reopen Booking", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel info = new JLabel("Choose new start and end dates:");
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(info);
        p.add(Box.createVerticalStrut(8));

        JSpinner sFrom = new JSpinner(new SpinnerDateModel(booking.getStartDate(), null, null, Calendar.DAY_OF_MONTH));
        sFrom.setEditor(new JSpinner.DateEditor(sFrom, "yyyy-MM-dd"));
        JSpinner sTo = new JSpinner(new SpinnerDateModel(booking.getEndDate(), null, null, Calendar.DAY_OF_MONTH));
        sTo.setEditor(new JSpinner.DateEditor(sTo, "yyyy-MM-dd"));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        row.add(new JLabel("Start:"));
        row.add(sFrom);
        row.add(new JLabel("End:"));
        row.add(sTo);
        p.add(row);

        p.add(Box.createVerticalStrut(12));

        JButton ok = new JButton("REOPEN");
        JButton cancel = new JButton("CANCEL");
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btns.add(cancel);
        btns.add(ok);
        p.add(btns);

        ok.addActionListener(evt -> {
            Date ns = (Date) sFrom.getValue();
            Date ne = (Date) sTo.getValue();
            if (ne.before(ns)) {
                JOptionPane.showMessageDialog(dlg, "End date must be same or after start date.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                boolean ok2 = bookingService.reopenBooking(bookingId, ns, ne);
                if (ok2) {
                    JOptionPane.showMessageDialog(dlg, "Booking reopened.");
                    dlg.dispose();
                    applyFiltersAndLoad();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Failed to reopen booking.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dlg, "Error reopening booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancel.addActionListener(evt -> dlg.dispose());

        dlg.setContentPane(p);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
    
    private void exportPdfCurrentFiltered() {
        if (currentSearchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records to export.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save PDF Report");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF files", "pdf"));
        int ret = fc.showSaveDialog(this);
        if (ret != JFileChooser.APPROVE_OPTION) return;
        
        String path = fc.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";
        
        try {
            // Create a temporary table model with all filtered data
            javax.swing.table.DefaultTableModel tempModel = new javax.swing.table.DefaultTableModel(
                new String[]{"Vehicle", "Start", "End", "Days", "Total", "Status"}, 0);
            
            // Add all filtered results to temp model
            for (Booking booking : currentSearchResults) {
                model.Vehicle vehicle = vehicleMap.get(booking.getVehicleId());
                String vehicleName = vehicle != null ? vehicle.getModel() : "Vehicle ID: " + booking.getVehicleId();
                long days = Math.max(1, (booking.getEndDate().getTime() - booking.getStartDate().getTime()) / (1000L * 60 * 60 * 24) + 1);
                
                Object[] row = {
                    vehicleName,
                    sdf.format(booking.getStartDate()),
                    sdf.format(booking.getEndDate()),
                    days,
                    String.format("%,.0f", booking.getTotalCost()),
                    booking.getStatus()
                };
                tempModel.addRow(row);
            }
            
            // Create temporary table with all data
            javax.swing.JTable tempTable = new javax.swing.JTable(tempModel);
            
            util.PDFExporter.exportTableToPDF(tempTable, "My Bookings Report (All Pages)", path);
            JOptionPane.showMessageDialog(this, "Report exported successfully to: " + path + "\nTotal records: " + currentSearchResults.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportCsvCurrentFiltered() {
        if (currentSearchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records to export.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save CSV Report");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        int ret = fc.showSaveDialog(this);
        if (ret != JFileChooser.APPROVE_OPTION) return;
        
        String path = fc.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) path += ".csv";
        
        try (java.io.FileWriter writer = new java.io.FileWriter(path)) {
            writer.append("HAPA Vehicle Rental System - My Bookings Report\n");
            writer.append("Generated on: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n\n");
            writer.append("Vehicle Model,Start Date,End Date,Days,Total Cost,Status\n");
            
            for (Booking booking : currentSearchResults) {
                model.Vehicle vehicle = vehicleMap.get(booking.getVehicleId());
                String vehicleName = vehicle != null ? vehicle.getModel() : "Unknown";
                long days = Math.max(1L, (booking.getEndDate().getTime() - booking.getStartDate().getTime())/(1000L*60*60*24) + 1);
                
                writer.append(vehicleName.replace(",", ";")).append(",");
                writer.append(sdf.format(booking.getStartDate())).append(",");
                writer.append(sdf.format(booking.getEndDate())).append(",");
                writer.append(String.valueOf(days)).append(",");
                writer.append(String.format("%,.0f RWF", booking.getTotalCost())).append(",");
                writer.append(booking.getStatus()).append("\n");
            }
            JOptionPane.showMessageDialog(this, "Report exported successfully to: " + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color line;

        RoundedBorder(int radius, Color lineColor) { 
            this.radius = radius; 
            this.line = lineColor; 
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(line);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(6, 8, 6, 8);
        }
    }
}