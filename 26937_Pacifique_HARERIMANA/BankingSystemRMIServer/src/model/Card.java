package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author jeremie
 */
@Entity
public class Card implements Serializable{
    
    public static final long serialVersionUID = 1L;
    
    @Id
    private String cardNumber;
    private ECardType type;
    private String ccv;
    private String pin;
    private String endMonth;
    private String endYear;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Card() {
    }

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Card(String cardNumber, ECardType type, String ccv, String pin, String endMonth, String endYear, boolean active, Account account) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.ccv = ccv;
        this.pin = pin;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.active = active;
        this.account = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ECardType getType() {
        return type;
    }

    public void setType(ECardType type) {
        this.type = type;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    
}
