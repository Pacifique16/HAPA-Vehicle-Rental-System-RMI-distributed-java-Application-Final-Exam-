package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author jeremie
 */
public class LoanRepayment implements Serializable{
    public static final long serialVersionUID = 1L;
    private int id;
    private LocalDate paymentDate;
    private Double paymentAmount;
    private Loan loan;

    public LoanRepayment() {
    }

    public LoanRepayment(int id) {
        this.id = id;
    }

    public LoanRepayment(int id, LocalDate paymentDate, Double paymentAmount, Loan loan) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.loan = loan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
    
    
}
