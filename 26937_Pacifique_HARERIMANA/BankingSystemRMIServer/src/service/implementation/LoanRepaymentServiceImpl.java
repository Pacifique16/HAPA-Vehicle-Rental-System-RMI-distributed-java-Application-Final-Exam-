package service.implementation;

import dao.LoanDao;
import dao.LoanRepaymentDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.ELoanStatus;
import model.Loan;
import model.LoanRepayment;
import service.LoanRepaymentService;

public class LoanRepaymentServiceImpl extends UnicastRemoteObject implements LoanRepaymentService{

    LoanRepaymentDao dao = new LoanRepaymentDao();
    LoanDao loanDao = new LoanDao();
    
    public LoanRepaymentServiceImpl() throws RemoteException{
    }

    @Override
    public LoanRepayment registerLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException {
        if(theLoanRepayment.getPaymentAmount() == null || theLoanRepayment.getPaymentAmount() <= 0){
            throw new RemoteException("Payment amount must be greater than zero");
        }
        
        Loan loan = loanDao.findLoanById(theLoanRepayment.getLoan());
        if(loan == null){
            throw new RemoteException("Loan not found");
        }
        
        if(loan.getStatus() == ELoanStatus.COMPLETED){
            throw new RemoteException("Loan already completed");
        }
        
        if(loan.getStatus() == ELoanStatus.REJECTED){
            throw new RemoteException("Cannot make payment on rejected loan");
        }
        
        if(loan.getStatus() == ELoanStatus.INITIATED){
            loan.setStatus(ELoanStatus.PROGRESS);
            loanDao.updateLoan(loan);
        }
        
        List<LoanRepayment> payments = loan.getRepayments();
        Double totalPaid = 0.0;
        if(payments != null){
            for(LoanRepayment payment : payments){
                totalPaid += payment.getPaymentAmount();
            }
        }
        
        Double remaining = loan.getAmountToPay() - totalPaid;
        if(theLoanRepayment.getPaymentAmount() > remaining){
            throw new RemoteException("Payment amount ($" + theLoanRepayment.getPaymentAmount() + ") exceeds remaining loan balance ($" + remaining + ")");
        }
        
        totalPaid += theLoanRepayment.getPaymentAmount();
        
        if(totalPaid >= loan.getAmountToPay()){
            loan.setStatus(ELoanStatus.COMPLETED);
            loanDao.updateLoan(loan);
        }
        
        return dao.createLoanRepayment(theLoanRepayment);
    }

    @Override
    public LoanRepayment updateLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException {
        return dao.updateLoanRepayment(theLoanRepayment);
    }

    @Override
    public LoanRepayment deleteLoanRepayment(LoanRepayment theLoanRepayment) throws RemoteException {
        return dao.deleteLoanRepayment(theLoanRepayment);
    }

    @Override
    public LoanRepayment searchLoanRepaymentById(LoanRepayment theLoanRepayment) throws RemoteException {
        return dao.findLoanRepaymentById(theLoanRepayment);
    }

    @Override
    public List<LoanRepayment> retrieveAllLoanRepayments() throws RemoteException {
        return dao.findAllLoanRepayments();
    }
}
