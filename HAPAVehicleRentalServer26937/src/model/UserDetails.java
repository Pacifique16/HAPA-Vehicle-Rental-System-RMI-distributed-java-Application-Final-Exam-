package model;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
public class UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "license_number")
    private String licenseNumber;
    
    public UserDetails() {}
    
    public UserDetails(User user, String address, String licenseNumber) {
        this.user = user;
        this.address = address;
        this.licenseNumber = licenseNumber;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
}