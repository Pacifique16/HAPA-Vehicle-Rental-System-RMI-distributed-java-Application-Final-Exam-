package service.implementation;

import dao.LoanDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import model.ELoanStatus;
import model.Loan;
import service.LoanService;

public class LoanServiceImpl extends UnicastRemoteObject implements LoanService{

    LoanDao dao = new LoanDao();
    public LoanServiceImpl() throws RemoteException{
    }

    @Override
    public Loan registerLoan(Loan theLoan) throws RemoteException {
        if(theLoan.getAmountToReceive() == null || theLoan.getAmountToReceive() <= 0){
            throw new RemoteException("Loan amount must be greater than zero");
        }
        
        if(theLoan.getInterestRate() == null || theLoan.getInterestRate() < 0){
            throw new RemoteException("Interest rate must be specified");
        }
        
        if(theLoan.getStartDate() == null || theLoan.getEndDate() == null){
            throw new RemoteException("Loan period must be specified");
        }
        
        if(theLoan.getEndDate().isBefore(theLoan.getStartDate())){
            throw new RemoteException("End date must be after start date");
        }
        
        Double principal = theLoan.getAmountToReceive();
        Double rate = theLoan.getInterestRate();
        Double totalAmount = principal + (principal * rate / 100);
        theLoan.setAmountToPay(totalAmount);
        
        long months = ChronoUnit.MONTHS.between(theLoan.getStartDate(), theLoan.getEndDate());
        if(months <= 0) months = 1;
        Double monthlyPayment = totalAmount / months;
        theLoan.setMonthlyDeduction(monthlyPayment);
        
        if(theLoan.getCreatedDate() == null){
            theLoan.setCreatedDate(LocalDate.now());
        }
        
        if(theLoan.getStatus() == null){
            theLoan.setStatus(ELoanStatus.INITIATED);
        }
        
        return dao.createLoan(theLoan);
    }

    @Override
    public Loan updateLoan(Loan theLoan) throws RemoteException {
        return dao.updateLoan(theLoan);
    }

    @Override
    public Loan deleteLoan(Loan theLoan) throws RemoteException {
        return dao.deleteLoan(theLoan);
    }

    @Override
    public Loan searchLoanById(Loan theLoan) throws RemoteException {
        return dao.findLoanById(theLoan);
    }

    @Override
    public List<Loan> retrieveAllLoans() throws RemoteException {
        return dao.findAllLoans();
    }
}
