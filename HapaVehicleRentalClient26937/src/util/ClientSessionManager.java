package util;

import model.Session;
import service.SessionService;
import java.rmi.Naming;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class ClientSessionManager {
    private static ClientSessionManager instance;
    private Session currentSession;
    private SessionService sessionService;
    private Timer activityTimer;
    private Timer timeoutWarningTimer;
    private Timer forceLogoutTimer;
    
    // Warning at 1 minute 40 seconds (20 seconds before timeout)
    private static final int WARNING_TIME = 100 * 1000; // 100 seconds
    
    private ClientSessionManager() {
        try {
            sessionService = (SessionService) Naming.lookup("rmi://localhost:3506/SessionService");
        } catch (Exception e) {
            System.err.println("Failed to connect to SessionService: " + e.getMessage());
        }
    }
    
    public static synchronized ClientSessionManager getInstance() {
        if (instance == null) {
            instance = new ClientSessionManager();
        }
        return instance;
    }
    
    public void startSession(Session session) {
        this.currentSession = session;
        startActivityTimer();
        startTimeoutWarning();
    }
    
    private void startActivityTimer() {
        // Update activity every 30 seconds
        activityTimer = new Timer(30 * 1000, e -> updateActivity());
        activityTimer.start();
    }
    
    private void startTimeoutWarning() {
        // Show warning after 40 seconds
        timeoutWarningTimer = new Timer(WARNING_TIME, e -> showTimeoutWarning());
        timeoutWarningTimer.setRepeats(false);
        timeoutWarningTimer.start();
        
        // Force logout after 2 minutes if no response
        forceLogoutTimer = new Timer(120 * 1000, e -> {
            if (currentSession != null) {
                handleSessionExpired();
            }
        });
        forceLogoutTimer.setRepeats(false);
        forceLogoutTimer.start();
    }
    
    private void updateActivity() {
        if (currentSession != null && sessionService != null) {
            try {
                sessionService.updateSessionActivity(currentSession.getSessionId());
            } catch (Exception e) {
                // Session might be expired
                handleSessionExpired();
            }
        }
    }
    
    private void showTimeoutWarning() {
        int choice = JOptionPane.showConfirmDialog(
            null,
            "Your session will expire in 20 seconds due to inactivity.\nDo you want to continue?",
            "Session Timeout Warning",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Extend session
            updateActivity();
            // Restart both timers
            timeoutWarningTimer.restart();
            if (forceLogoutTimer != null) {
                forceLogoutTimer.restart();
            }
        } else {
            // User chose to logout
            logout();
        }
    }
    
    private void handleSessionExpired() {
        JOptionPane.showMessageDialog(
            null,
            "Your session has expired. Please login again.",
            "Session Expired",
            JOptionPane.WARNING_MESSAGE
        );
        logout();
    }
    
    public void logout() {
        if (currentSession != null && sessionService != null) {
            try {
                sessionService.invalidateSession(currentSession.getSessionId());
            } catch (Exception e) {
                // Ignore errors during logout
            }
        }
        
        stopTimers();
        currentSession = null;
        
        // Return to login screen
        new view.LoginForm().setVisible(true);
        
        // Close all other windows
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            if (!(window instanceof view.LoginForm)) {
                window.dispose();
            }
        }
    }
    
    private void stopTimers() {
        if (activityTimer != null) {
            activityTimer.stop();
        }
        if (timeoutWarningTimer != null) {
            timeoutWarningTimer.stop();
        }
        if (forceLogoutTimer != null) {
            forceLogoutTimer.stop();
        }
    }
    
    public boolean isSessionValid() {
        if (currentSession == null || sessionService == null) {
            return false;
        }
        
        try {
            return sessionService.validateSession(currentSession.getSessionId());
        } catch (Exception e) {
            return false;
        }
    }
    
    public Session getCurrentSession() {
        return currentSession;
    }
}