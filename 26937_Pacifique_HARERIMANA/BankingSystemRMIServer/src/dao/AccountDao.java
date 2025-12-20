package dao;

import java.util.List;
import model.Account;
import model.Customer;
import model.Loan;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class AccountDao {
    // CRUD operations
    // CREATE
    public Account createAccount(Account accountObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(accountObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return accountObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public Account updateAccount(Account accountObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(accountObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return accountObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public Account deleteAccount(Account accountObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(accountObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return accountObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public Account findAccountById(Account accountObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //Account theAccount = (Account) ss.createQuery("SELECT acc FROM Account acc WHERE acc.accountNumber='"+accountObj.getAccountNumber()+"'");
            Account theAccount = (Account)ss.get(Account.class ,accountObj.getAccountNumber());
            ss.close();
            return theAccount;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<Account> findAllAccounts(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<Account> accounts = ss.createQuery("SELECT acc FROM Account acc").list();
            ss.close();
            return accounts;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public Account findAccountWithDetails(Account accountObj){
        try{
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Account account = (Account)ss.get(Account.class, accountObj.getAccountNumber());
            
            if(account != null){
                Hibernate.initialize(account.getCustomers());
                System.out.println("Server - Customers loaded: " + (account.getCustomers() != null ? account.getCustomers().size() : "null"));
                if(account.getCustomers() != null){
                    for(Customer c : account.getCustomers()){
                        Hibernate.initialize(c.getRiskProfile());
                    }
                }
                Hibernate.initialize(account.getCards());
                System.out.println("Server - Cards loaded: " + (account.getCards() != null ? account.getCards().size() : "null"));
                Hibernate.initialize(account.getLoans());
                System.out.println("Server - Loans loaded: " + (account.getLoans() != null ? account.getLoans().size() : "null"));
                if(account.getLoans() != null){
                    for(Loan loan : account.getLoans()){
                        Hibernate.initialize(loan.getRepayments());
                        System.out.println("Server - Loan " + loan.getId() + " repayments: " + (loan.getRepayments() != null ? loan.getRepayments().size() : "null"));
                    }
                }
            }
            
            ss.close();
            return account;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
