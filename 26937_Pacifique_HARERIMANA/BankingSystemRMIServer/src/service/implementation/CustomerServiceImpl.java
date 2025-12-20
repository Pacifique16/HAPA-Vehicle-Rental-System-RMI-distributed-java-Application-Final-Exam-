
package service.implementation;

import dao.CustomerDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Customer;
import service.CustomerService;

/**
 *
 * @author jeremie
 */
public class CustomerServiceImpl extends UnicastRemoteObject implements CustomerService{

    CustomerDao dao = new CustomerDao();
    public CustomerServiceImpl() throws RemoteException{
    }

    
    @Override
    public Customer registerCustomer(Customer theCustomer) throws RemoteException {
        return dao.createCustomer(theCustomer);
    }

    @Override
    public Customer updateCustomer(Customer theCustomer) throws RemoteException {
        return dao.updateCustomer(theCustomer);
    }

    @Override
    public Customer deleteCustomer(Customer theCustomer) throws RemoteException {
        return dao.deleteCustomer(theCustomer);
    }

    @Override
    public Customer searchCustomerById(Customer theCustomer) throws RemoteException {
        return dao.findCustomerById(theCustomer);
    }

    @Override
    public List<Customer> retrieveAllCustomers() throws RemoteException {
        return dao.findAllCustomers();
    }

    @Override
    public Customer searchCustomerByFirstName(String firstName) throws RemoteException {
        return dao.findCustomerByFirstName(firstName);
    }
}
