package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author jeremie
 */
@Entity
@Table(name = "risk_profile")
public class RiskProfile implements Serializable{
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private ERiskType type;
    @OneToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    public RiskProfile() {
    }

    public RiskProfile(int id) {
        this.id = id;
    }

    public RiskProfile(int id, ERiskType type, Customer customer) {
        this.id = id;
        this.type = type;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ERiskType getType() {
        return type;
    }

    public void setType(ERiskType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
