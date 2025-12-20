package service.implementation;

import dao.AccountDao;
import dao.TransactionDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Account;
import model.ETransactionStatus;
import model.ETransactionType;
import model.Transaction;
import service.TransactionService;

public class TransactionServiceImpl extends UnicastRemoteObject implements TransactionService{

    TransactionDao dao = new TransactionDao();
    AccountDao accountDao = new AccountDao();
    
    public TransactionServiceImpl() throws RemoteException{
    }

    @Override
    public Transaction registerTransaction(Transaction theTransaction) throws RemoteException {
        Account account = accountDao.findAccountById(theTransaction.getAccount());
        
        if(account == null){
            throw new RemoteException("Account not found");
        }
        
        if(!account.isActive()){
            throw new RemoteException("Account is inactive. Cannot process transaction.");
        }
        
        Double currentBalance = account.getBalance();
        Double amount = theTransaction.getAmount();
        
        if(theTransaction.getType() == ETransactionType.DEPOSIT){
            account.setBalance(currentBalance + amount);
            theTransaction.setStatus(ETransactionStatus.SUCCESSFUL);
        }
        else if(theTransaction.getType() == ETransactionType.WITHDRAW){
            if(currentBalance < amount){
                theTransaction.setStatus(ETransactionStatus.FAILED);
                Transaction failed = dao.createTransaction(theTransaction);
                throw new RemoteException("Insufficient balance. Current: " + currentBalance + ", Requested: " + amount);
            }
            account.setBalance(currentBalance - amount);
            theTransaction.setStatus(ETransactionStatus.SUCCESSFUL);
        }
        else if(theTransaction.getType() == ETransactionType.TRANSFER){
            if(currentBalance < amount){
                theTransaction.setStatus(ETransactionStatus.FAILED);
                Transaction failed = dao.createTransaction(theTransaction);
                throw new RemoteException("Insufficient balance for transfer");
            }
            account.setBalance(currentBalance - amount);
            theTransaction.setStatus(ETransactionStatus.SUCCESSFUL);
        }
        
        accountDao.updateAccount(account);
        return dao.createTransaction(theTransaction);
    }

    @Override
    public Transaction updateTransaction(Transaction theTransaction) throws RemoteException {
        return dao.updateTransaction(theTransaction);
    }

    @Override
    public Transaction deleteTransaction(Transaction theTransaction) throws RemoteException {
        return dao.deleteTransaction(theTransaction);
    }

    @Override
    public Transaction searchTransactionById(Transaction theTransaction) throws RemoteException {
        return dao.findTransactionById(theTransaction);
    }

    @Override
    public List<Transaction> retrieveAllTransactions() throws RemoteException {
        return dao.findAllTransactions();
    }
}
