package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Card;

public interface CardService extends Remote{
    public Card registerCard(Card theCard) throws RemoteException;
    public Card updateCard(Card theCard) throws RemoteException;
    public Card deleteCard(Card theCard) throws RemoteException;
    public Card searchCardById(Card theCard) throws RemoteException;
    public List<Card> retrieveAllCards() throws RemoteException;
}
