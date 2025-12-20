package service.implementation;

import dao.AccountDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
import model.Account;
import service.AccountService;

public class AccountServiceImpl extends UnicastRemoteObject implements AccountService{

    AccountDao dao = new AccountDao();
    public AccountServiceImpl() throws RemoteException{
    }

    @Override
    public Account registerAccount(Account theAccount) throws RemoteException {
        if(theAccount.getAccountNumber() == null || theAccount.getAccountNumber().trim().isEmpty()){
            throw new RemoteException("Account number is required");
        }
        
        if(theAccount.getBalance() == null || theAccount.getBalance() < 0){
            throw new RemoteException("Initial balance cannot be negative");
        }
        
        if(theAccount.getType() == null){
            throw new RemoteException("Account type is required");
        }
        
        if(theAccount.getRegisteredDate() == null){
            theAccount.setRegisteredDate(LocalDate.now());
        }
        
        return dao.createAccount(theAccount);
    }

    @Override
    public Account updateAccount(Account theAccount) throws RemoteException {
        return dao.updateAccount(theAccount);
    }

    @Override
    public Account deleteAccount(Account theAccount) throws RemoteException {
        return dao.deleteAccount(theAccount);
    }

    @Override
    public Account searchAccountById(Account theAccount) throws RemoteException {
        return dao.findAccountById(theAccount);
    }

    @Override
    public List<Account> retrieveAllAccounts() throws RemoteException {
        return dao.findAllAccounts();
    }

    @Override
    public Account getAccountWithDetails(Account theAccount) throws RemoteException {
        return dao.findAccountWithDetails(theAccount);
    }
}
