package model;

import java.io.Serializable;

/**
 *
 * @author jeremie
 */
public class RiskProfile implements Serializable{
    public static final long serialVersionUID = 1L;
    private int id;
    private ERiskType type;
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
