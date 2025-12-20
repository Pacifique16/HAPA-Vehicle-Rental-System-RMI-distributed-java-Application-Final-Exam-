package model;

import java.io.Serializable;

public class SimpleVehicle implements Serializable {
    
    private int id;
    private String plateNumber;
    private String model;
    private String category;
    private String fuelType;
    private String transmission;
    private int seats;
    private double pricePerDay;
    private String imagePath;
    private String status = "Available";
    
    public SimpleVehicle() {}
    
    public SimpleVehicle(String plateNumber, String model, String category, double pricePerDay) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.category = category;
        this.pricePerDay = pricePerDay;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    
    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}