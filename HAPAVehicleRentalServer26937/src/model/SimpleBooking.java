package model;

import java.io.Serializable;
import java.util.Date;

public class SimpleBooking implements Serializable {
    
    private int id;
    private SimpleUser customer;
    private SimpleVehicle vehicle;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private String status = "PENDING";
    private String rejectionReason;
    private Date bookingDate = new Date();
    
    public SimpleBooking() {}
    
    public SimpleBooking(SimpleUser customer, SimpleVehicle vehicle, Date startDate, Date endDate, double totalCost) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public SimpleUser getCustomer() { return customer; }
    public void setCustomer(SimpleUser customer) { this.customer = customer; }
    
    public SimpleVehicle getVehicle() { return vehicle; }
    public void setVehicle(SimpleVehicle vehicle) { this.vehicle = vehicle; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
    
    // Helper methods for backward compatibility
    public int getCustomerId() { return customer != null ? customer.getId() : 0; }
    public int getVehicleId() { return vehicle != null ? vehicle.getId() : 0; }
}