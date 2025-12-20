package service.implementation;

import dao.AccountDao;
import dao.CardDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Account;
import model.Card;
import service.CardService;

public class CardServiceImpl extends UnicastRemoteObject implements CardService{

    CardDao dao = new CardDao();
    AccountDao accountDao = new AccountDao();
    
    public CardServiceImpl() throws RemoteException{
    }

    @Override
    public Card registerCard(Card theCard) throws RemoteException {
        if(theCard.getCardNumber() == null || !theCard.getCardNumber().matches("\\d{16}")){
            throw new RemoteException("Card number must be 16 digits");
        }
        
        if(theCard.getCcv() == null || !theCard.getCcv().matches("\\d{3}")){
            throw new RemoteException("CCV must be 3 digits");
        }
        
        if(theCard.getPin() == null || !theCard.getPin().matches("\\d{4}")){
            throw new RemoteException("PIN must be 4 digits");
        }
        
        Account account = accountDao.findAccountById(theCard.getAccount());
        if(account == null){
            throw new RemoteException("Account not found");
        }
        
        if(!account.isActive()){
            throw new RemoteException("Cannot issue card for inactive account");
        }
        
        if(account.getBalance() < 1000){
            throw new RemoteException("Minimum balance of 1000 required to issue card");
        }
        
        return dao.createCard(theCard);
    }

    @Override
    public Card updateCard(Card theCard) throws RemoteException {
        return dao.updateCard(theCard);
    }

    @Override
    public Card deleteCard(Card theCard) throws RemoteException {
        return dao.deleteCard(theCard);
    }

    @Override
    public Card searchCardById(Card theCard) throws RemoteException {
        return dao.findCardById(theCard);
    }

    @Override
    public List<Card> retrieveAllCards() throws RemoteException {
        return dao.findAllCards();
    }
}
