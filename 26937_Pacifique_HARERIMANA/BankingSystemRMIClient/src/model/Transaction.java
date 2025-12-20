package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author jeremie
 */
public class Transaction implements Serializable{
    
    public static final long serialVersionUID = 1L;
    private int id;
    private ETransactionType type;
    private Double amount;
    private LocalDate transactionDate;
    private ETransactionStatus status;
    private Account account;

    public Transaction() {
    }

    public Transaction(int id) {
        this.id = id;
    }

    public Transaction(int id, ETransactionType type, Double amount, LocalDate transactionDate, ETransactionStatus status, Account account) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ETransactionType getType() {
        return type;
    }

    public void setType(ETransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    
}
