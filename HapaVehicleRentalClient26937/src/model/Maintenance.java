package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "maintenance")
public class Maintenance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "maintenance_type")
    private String maintenanceType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "cost")
    private double cost;
    
    @Column(name = "maintenance_date")
    private String maintenanceDate;
    
    @ManyToMany(mappedBy = "maintenanceRecords")
    private Set<Vehicle> vehicles = new HashSet<>();
    
    // Constructors
    public Maintenance() {}
    
    public Maintenance(String maintenanceType, String description, double cost, String maintenanceDate) {
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.cost = cost;
        this.maintenanceDate = maintenanceDate;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    
    public String getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(String maintenanceDate) { this.maintenanceDate = maintenanceDate; }
    
    public Set<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(Set<Vehicle> vehicles) { this.vehicles = vehicles; }
}