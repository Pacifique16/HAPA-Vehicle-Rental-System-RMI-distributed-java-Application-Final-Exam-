package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Loan;

public interface LoanService extends Remote{
    public Loan registerLoan(Loan theLoan) throws RemoteException;
    public Loan updateLoan(Loan theLoan) throws RemoteException;
    public Loan deleteLoan(Loan theLoan) throws RemoteException;
    public Loan searchLoanById(Loan theLoan) throws RemoteException;
    public List<Loan> retrieveAllLoans() throws RemoteException;
}
