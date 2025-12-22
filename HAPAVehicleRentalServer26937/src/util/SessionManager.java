package util;

import model.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;
    private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    private SessionManager() {
        // Clean expired sessions every 5 minutes
        scheduler.scheduleAtFixedRate(this::cleanExpiredSessions, 5, 5, TimeUnit.MINUTES);
    }
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public Session createSession(int userId, String username, String role, String clientIP) {
        // Invalidate any existing sessions for this user (single session per user)
        invalidateUserSessions(userId);
        
        Session session = new Session(userId, username, role, clientIP);
        activeSessions.put(session.getSessionId(), session);
        return session;
    }
    
    public Session getSession(String sessionId) {
        Session session = activeSessions.get(sessionId);
        if (session != null && session.isActive() && !session.isExpired()) {
            session.updateActivity();
            return session;
        }
        return null;
    }
    
    public boolean isValidSession(String sessionId) {
        return getSession(sessionId) != null;
    }
    
    public void invalidateSession(String sessionId) {
        Session session = activeSessions.get(sessionId);
        if (session != null) {
            session.invalidate();
            activeSessions.remove(sessionId);
        }
    }
    
    public void invalidateUserSessions(int userId) {
        activeSessions.values().removeIf(session -> 
            session.getUserId() == userId);
    }
    
    private void cleanExpiredSessions() {
        activeSessions.values().removeIf(session -> 
            session.isExpired() || !session.isActive());
    }
    
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
}