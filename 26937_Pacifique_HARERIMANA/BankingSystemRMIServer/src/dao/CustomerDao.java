package dao;

import java.util.List;
import model.Customer;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class CustomerDao {
    // CRUD operations
    // CREATE
    public Customer createCustomer(Customer customerObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(customerObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return customerObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public Customer updateCustomer(Customer customerObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(customerObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return customerObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public Customer deleteCustomer(Customer customerObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(customerObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return customerObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public Customer findCustomerById(Customer customerObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //Customer theCustomer = (Customer) ss.createQuery("SELECT acc FROM Customer acc WHERE acc.customerNumber='"+customerObj.getCustomerNumber()+"'");
            Customer theCustomer = (Customer)ss.get(Customer.class ,customerObj.getId());
            ss.close();
            return theCustomer;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<Customer> findAllCustomers(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<Customer> customers = ss.createQuery("SELECT acc FROM Customer acc").list();
            ss.close();
            return customers;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    // FIND BY FIRSTNAME
    public Customer findCustomerByFirstName(String firstName){
        try{
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Customer theCustomer = (Customer) ss.createQuery("FROM Customer WHERE firstName = :fname")
                    .setParameter("fname", firstName)
                    .uniqueResult();
            ss.close();
            return theCustomer;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
