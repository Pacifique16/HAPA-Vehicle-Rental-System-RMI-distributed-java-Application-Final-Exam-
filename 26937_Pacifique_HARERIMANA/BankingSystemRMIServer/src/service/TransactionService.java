package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Transaction;

public interface TransactionService extends Remote{
    public Transaction registerTransaction(Transaction theTransaction) throws RemoteException;
    public Transaction updateTransaction(Transaction theTransaction) throws RemoteException;
    public Transaction deleteTransaction(Transaction theTransaction) throws RemoteException;
    public Transaction searchTransactionById(Transaction theTransaction) throws RemoteException;
    public List<Transaction> retrieveAllTransactions() throws RemoteException;
}
