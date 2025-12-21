package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;
    
    @Column(name = "model", nullable = false)
    private String model;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "fuel_type")
    private String fuelType;
    
    @Column(name = "transmission")
    private String transmission;
    
    @Column(name = "seats")
    private int seats;
    
    @Column(name = "price_per_day", nullable = false)
    private double pricePerDay;
    
    @Column(name = "image_path")
    private String imagePath;
    
    @Column(name = "status")
    private String status = "Available";
    
    // One-to-Many: One vehicle can have many bookings
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
    
    public Vehicle() {}
    
    public Vehicle(String plateNumber, String model, String category, double pricePerDay) {
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