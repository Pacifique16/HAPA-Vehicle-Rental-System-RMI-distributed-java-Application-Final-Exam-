package dao;

import java.util.List;
import model.Card;
import org.hibernate.*;

/**
 *
 * @author jeremie
 */
public class CardDao {
    // CRUD operations
    // CREATE
    public Card createCard(Card cardObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.save(cardObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return cardObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // UPDATE
    public Card updateCard(Card cardObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.update(cardObj);
            // step 4: commit transaction
            tr.commit();
            // step 5: close session
            ss.close();
            return cardObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // DELETE
    public Card deleteCard(Card cardObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            // step 2: create transaction
            //Transaction tr = ss.beginTransaction();
            // step 3: perfom action
            ss.delete(cardObj);
            // step 4: commit transaction
            //tr.commit();
            ss.beginTransaction().commit();
            // step 5: close session
            ss.close();
            return cardObj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // FIND BY ID
    public Card findCardById(Card cardObj){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            //Card theCard = (Card) ss.createQuery("SELECT acc FROM Card acc WHERE acc.cardNumber='"+cardObj.getCardNumber()+"'");
            Card theCard = (Card)ss.get(Card.class ,cardObj.getCardNumber());
            ss.close();
            return theCard;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    // Retrieve
    public List<Card> findAllCards(){
        try{
            // step1 create session
            Session ss = HibernateUtil.getSessionFactory()
                    .openSession();
            List<Card> cards = ss.createQuery("SELECT c FROM Card c").list();
            ss.close();
            return cards;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
