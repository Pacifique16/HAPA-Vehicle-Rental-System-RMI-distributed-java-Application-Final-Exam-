package dao;

import java.util.List;
import model.Loan;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class LoanDao {
    // CRUD operations
    // CREATE
    public Loan createLoan(Loan loanObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(loanObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return loanObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public Loan updateLoan(Loan loanObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(loanObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return loanObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public Loan deleteLoan(Loan loanObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(loanObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return loanObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public Loan findLoanById(Loan loanObj){
        try{
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Loan theLoan = (Loan)ss.createQuery("SELECT l FROM Loan l LEFT JOIN FETCH l.repayments WHERE l.id = :id")
                    .setParameter("id", loanObj.getId())
                    .uniqueResult();
            ss.close();
            return theLoan;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<Loan> findAllLoans(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<Loan> loans = ss.createQuery("SELECT acc FROM Loan acc").list();
            ss.close();
            return loans;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
