package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.LoanRepayment;

public interface LoanRepaymentService extends Remote{
    public LoanRepayment registerLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException;
    public LoanRepayment updateLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException;
    public LoanRepayment deleteLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException;
    public LoanRepayment searchLoanRepaymentById(LoanRepayment theLoanRepayment) throws RemoteException;
    public List<LoanRepayment> retrieveAllLoanRepayments() throws RemoteException;
}
