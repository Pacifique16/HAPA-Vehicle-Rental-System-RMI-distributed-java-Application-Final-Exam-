/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Customer;

/**
 *
 * @author jeremie
 */
public interface CustomerService extends Remote{
    public Customer registerCustomer(Customer theCustomer) throws RemoteException;
    public Customer updateCustomer(Customer theCustomer) throws RemoteException;
    public Customer deleteCustomer(Customer theCustomer) throws RemoteException;
    public Customer searchCustomerById(Customer theCustomer) throws RemoteException;
    public List<Customer> retrieveAllCustomers() throws RemoteException;
    public Customer searchCustomerByFirstName(String firstName) throws RemoteException;
}
