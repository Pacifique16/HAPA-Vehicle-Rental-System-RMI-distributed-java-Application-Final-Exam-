/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Account;

/**
 *
 * @author jeremie
 */
public interface AccountService extends Remote{
    // method signature / definition
    public Account registerAccount(Account theAccount) throws RemoteException;
    public Account updateAccount(Account theAccount) throws RemoteException;
    public Account deleteAccount(Account theAccount) throws RemoteException;
    public Account searchAccountById(Account theAccount) throws RemoteException;
    public List<Account> retrieveAllAccounts() throws RemoteException;
    public Account getAccountWithDetails(Account theAccount) throws RemoteException;
}
