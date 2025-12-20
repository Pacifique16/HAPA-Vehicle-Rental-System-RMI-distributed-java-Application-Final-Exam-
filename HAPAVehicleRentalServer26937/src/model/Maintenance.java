package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "maintenance")
public class Maintenance implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "maintenance_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date maintenanceDate;
    
    @Column(nullable = false)
    private double cost;
    
    @Column(name = "performed_by")
    private String performedBy;
    
    private String status = "SCHEDULED"; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    
    @Column(name = "next_maintenance_date")
    @Temporal(TemporalType.DATE)
    private Date nextMaintenanceDate;
    
    // Many-to-Many relationship with Vehicle
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "vehicle_maintenance",
        joinColumns = @JoinColumn(name = "maintenance_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private List<Vehicle> vehicles;
    
    public Maintenance() {}
    
    public Maintenance(String maintenanceType, String description, Date maintenanceDate, double cost) {
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.cost = cost;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(Date maintenanceDate) { this.maintenanceDate = maintenanceDate; }
    
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(Date nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }
    
    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }
}