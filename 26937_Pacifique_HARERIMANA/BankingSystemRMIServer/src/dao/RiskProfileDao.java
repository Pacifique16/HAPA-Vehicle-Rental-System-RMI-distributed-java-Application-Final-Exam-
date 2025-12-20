package dao;

import java.util.List;
import model.RiskProfile;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class RiskProfileDao {
    // CRUD operations
    // CREATE
    public RiskProfile createRiskProfile(RiskProfile riskProfileObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(riskProfileObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return riskProfileObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public RiskProfile updateRiskProfile(RiskProfile riskProfileObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(riskProfileObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return riskProfileObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public RiskProfile deleteRiskProfile(RiskProfile riskProfileObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(riskProfileObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return riskProfileObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public RiskProfile findRiskProfileById(RiskProfile riskProfileObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //RiskProfile theRiskProfile = (RiskProfile) ss.createQuery("SELECT acc FROM RiskProfile acc WHERE acc.riskProfileNumber='"+riskProfileObj.getRiskProfileNumber()+"'");
            RiskProfile theRiskProfile = (RiskProfile)ss.get(RiskProfile.class ,riskProfileObj.getId());
            ss.close();
            return theRiskProfile;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<RiskProfile> findAllRiskProfiles(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<RiskProfile> riskProfiles = ss.createQuery("SELECT acc FROM RiskProfile acc").list();
            ss.close();
            return riskProfiles;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public RiskProfile findByCustomerId(int customerId){
        try{
            Session ss = HibernateUtil.getSessionFactory().openSession();
            RiskProfile profile = (RiskProfile)ss.createQuery(
                "SELECT r FROM RiskProfile r WHERE r.customer.id = :custId")
                .setParameter("custId", customerId)
                .uniqueResult();
            ss.close();
            return profile;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
