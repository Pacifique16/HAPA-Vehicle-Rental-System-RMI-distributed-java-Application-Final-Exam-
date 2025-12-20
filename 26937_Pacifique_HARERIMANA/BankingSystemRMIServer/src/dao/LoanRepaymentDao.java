package dao;

import java.util.List;
import model.LoanRepayment;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class LoanRepaymentDao {
    // CRUD operations
    // CREATE
    public LoanRepayment createLoanRepayment(LoanRepayment loanRepaymentObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(loanRepaymentObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return loanRepaymentObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public LoanRepayment updateLoanRepayment(LoanRepayment loanRepaymentObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(loanRepaymentObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return loanRepaymentObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public LoanRepayment deleteLoanRepayment(LoanRepayment loanRepaymentObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(loanRepaymentObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return loanRepaymentObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public LoanRepayment findLoanRepaymentById(LoanRepayment loanRepaymentObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //LoanRepayment theLoanRepayment = (LoanRepayment) ss.createQuery("SELECT acc FROM LoanRepayment acc WHERE acc.loanRepaymentNumber='"+loanRepaymentObj.getLoanRepaymentNumber()+"'");
            LoanRepayment theLoanRepayment = (LoanRepayment)ss.get(LoanRepayment.class ,loanRepaymentObj.getId());
            ss.close();
            return theLoanRepayment;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<LoanRepayment> findAllLoanRepayments(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<LoanRepayment> loanRepayments = ss.createQuery("SELECT acc FROM LoanRepayment acc").list();
            ss.close();
            return loanRepayments;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
