package service;

import model.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionService extends Remote {
    Session createSession(int userId, String username, String role, String clientIP) throws RemoteException;
    boolean validateSession(String sessionId) throws RemoteException;
    void invalidateSession(String sessionId) throws RemoteException;
    void updateSessionActivity(String sessionId) throws RemoteException;
    Session getSessionInfo(String sessionId) throws RemoteException;
}