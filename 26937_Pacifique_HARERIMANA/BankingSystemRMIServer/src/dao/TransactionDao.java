package dao;

import java.util.List;
import model.Transaction;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class TransactionDao {
    // CRUD operations
    // CREATE
    public Transaction createTransaction(Transaction transactionObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
//            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(transactionObj);
            // step 4: commit transaction
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return transactionObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public Transaction updateTransaction(Transaction transactionObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
//            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(transactionObj);
            // step 4: commit transaction
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return transactionObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public Transaction deleteTransaction(Transaction transactionObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(transactionObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return transactionObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public Transaction findTransactionById(Transaction transactionObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //Transaction theTransaction = (Transaction) ss.createQuery("SELECT acc FROM Transaction acc WHERE acc.transactionNumber='"+transactionObj.getTransactionNumber()+"'");
            Transaction theTransaction = (Transaction)ss.get(Transaction.class ,transactionObj.getId());
            ss.close();
            return theTransaction;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<Transaction> findAllTransactions(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<Transaction> transactions = ss.createQuery("SELECT acc FROM Transaction acc").list();
            ss.close();
            return transactions;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
