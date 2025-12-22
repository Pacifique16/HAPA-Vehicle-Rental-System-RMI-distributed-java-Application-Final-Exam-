package model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Session implements Serializable {
    private String sessionId;
    private int userId;
    private String username;
    private String role;
    private Date loginTime;
    private Date lastActivity;
    private String clientIP;
    private boolean active;
    
    // Session timeout: 1 minute
    private static final long SESSION_TIMEOUT = 1 * 60 * 1000;
    
    public Session(int userId, String username, String role, String clientIP) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.clientIP = clientIP;
        this.loginTime = new Date();
        this.lastActivity = new Date();
        this.active = true;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - lastActivity.getTime() > SESSION_TIMEOUT;
    }
    
    public void updateActivity() {
        this.lastActivity = new Date();
    }
    
    public void invalidate() {
        this.active = false;
    }
    
    // Getters and setters
    public String getSessionId() { return sessionId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Date getLoginTime() { return loginTime; }
    public Date getLastActivity() { return lastActivity; }
    public String getClientIP() { return clientIP; }
    public boolean isActive() { return active; }
    
    public void setActive(boolean active) { this.active = active; }
}