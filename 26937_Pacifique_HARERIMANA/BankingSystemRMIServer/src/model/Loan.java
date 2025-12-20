package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Loan implements Serializable{
    
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate createdDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double amountToPay;
    private ELoanStatus status;
    private Double interestRate;
    private Double amountToReceive;
    private Double monthlyDeduction;
    @OneToMany(mappedBy = "loan")
    private List<LoanRepayment> repayments;
    @ManyToMany
    @JoinTable(
            name = "loan_registered_to",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> accounts;

    public Loan() {
    }

    public Loan(int id) {
        this.id = id;
    }

    public Loan(int id, LocalDate createdDate, LocalDate startDate, LocalDate endDate, Double amountToPay, ELoanStatus status, Double interestRate, Double amountToReceive, Double monthlyDeduction, List<LoanRepayment> repayments, Set<Account> accounts) {
        this.id = id;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountToPay = amountToPay;
        this.status = status;
        this.interestRate = interestRate;
        this.amountToReceive = amountToReceive;
        this.monthlyDeduction = monthlyDeduction;
        this.repayments = repayments;
        this.accounts = accounts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public ELoanStatus getStatus() {
        return status;
    }

    public void setStatus(ELoanStatus status) {
        this.status = status;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getAmountToReceive() {
        return amountToReceive;
    }

    public void setAmountToReceive(Double amountToReceive) {
        this.amountToReceive = amountToReceive;
    }

    public Double getMonthlyDeduction() {
        return monthlyDeduction;
    }

    public void setMonthlyDeduction(Double monthlyDeduction) {
        this.monthlyDeduction = monthlyDeduction;
    }

    public List<LoanRepayment> getRepayments() {
        return repayments;
    }

    public void setRepayments(List<LoanRepayment> repayments) {
        this.repayments = repayments;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
    
    
}
