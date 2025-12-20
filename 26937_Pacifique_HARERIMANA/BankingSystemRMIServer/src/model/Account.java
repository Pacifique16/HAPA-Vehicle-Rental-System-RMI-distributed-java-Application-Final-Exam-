package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author jeremie
 */
@Entity
public class Account implements Serializable{
    
    public static final long serialVersionUID = 1L;
    @Id
    private String accountNumber;
    private Double balance;
    private LocalDate registeredDate;
    private boolean active;
    private EAccountType type;
    @OneToMany(mappedBy = "account")
    private List<Card> cards;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
    @ManyToMany
    @JoinTable(
            name = "customer_has_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "cust_id")
    )
    private Set<Customer> customers;
    
    @ManyToMany(mappedBy = "accounts")
    private Set<Loan> loans;

    public Account() {
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account(String accountNumber, Double balance, LocalDate registeredDate, boolean active, EAccountType type, List<Card> cards, List<Transaction> transactions, Set<Customer> customers, Set<Loan> loans) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.registeredDate = registeredDate;
        this.active = active;
        this.type = type;
        this.cards = cards;
        this.transactions = transactions;
        this.customers = customers;
        this.loans = loans;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EAccountType getType() {
        return type;
    }

    public void setType(EAccountType type) {
        this.type = type;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }
    
    
}
