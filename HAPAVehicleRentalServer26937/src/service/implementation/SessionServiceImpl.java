package service.implementation;

import service.SessionService;
import model.Session;
import util.SessionManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SessionServiceImpl extends UnicastRemoteObject implements SessionService {
    
    private SessionManager sessionManager;
    
    public SessionServiceImpl() throws RemoteException {
        super();
        this.sessionManager = SessionManager.getInstance();
    }
    
    @Override
    public Session createSession(int userId, String username, String role, String clientIP) throws RemoteException {
        return sessionManager.createSession(userId, username, role, clientIP);
    }
    
    @Override
    public boolean validateSession(String sessionId) throws RemoteException {
        return sessionManager.isValidSession(sessionId);
    }
    
    @Override
    public void invalidateSession(String sessionId) throws RemoteException {
        sessionManager.invalidateSession(sessionId);
    }
    
    @Override
    public void updateSessionActivity(String sessionId) throws RemoteException {
        Session session = sessionManager.getSession(sessionId);
        if (session != null) {
            session.updateActivity();
        }
    }
    
    @Override
    public Session getSessionInfo(String sessionId) throws RemoteException {
        return sessionManager.getSession(sessionId);
    }
}