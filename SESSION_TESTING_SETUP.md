# Session Management - Updated for Testing ⏰

## ✅ **Changes Made:**

### **Server Side** (`Session.java`):
- **Session timeout**: 30 minutes → **3 minutes**
- **Cleanup interval**: Still 5 minutes (unchanged)

### **Client Side** (`ClientSessionManager.java`):
- **Warning time**: 25 minutes → **2 minutes**
- **Activity updates**: 2 minutes → **30 seconds**
- **Warning message**: "5 minutes remaining" → **"1 minute remaining"**

## 🕐 **New Timeline:**

```
00:00 - User logs in → Session created
00:30 - First activity update sent
01:00 - Second activity update sent
01:30 - Third activity update sent
02:00 - WARNING: "Session expires in 1 minute"
02:30 - Activity update sent (if user continues)
03:00 - Session expires → Auto logout (if no activity)
```

## 🧪 **Testing Steps:**

1. **Build both projects** (server + client)
2. **Start server** → Run Server.java
3. **Start client** → Run LoginForm.java
4. **Login** with valid credentials
5. **Wait 2 minutes** → Warning dialog appears
6. **Choose option**:
   - **"Yes"** → Session extends, timer resets
   - **"No"** → Immediate logout
7. **Wait 3 minutes total** → Auto logout if no activity

## ⚡ **Quick Test Results:**

| Time | Expected Behavior |
|------|-------------------|
| 0:30 | Activity update (background) |
| 1:00 | Activity update (background) |
| 1:30 | Activity update (background) |
| 2:00 | ⚠️ **WARNING DIALOG** appears |
| 3:00 | 🚪 **AUTO LOGOUT** (if no response) |

## 🔄 **To Restore Production Settings:**

Change back to:
```java
// Session timeout: 30 minutes
private static final long SESSION_TIMEOUT = 30 * 60 * 1000;

// Warning at 25 minutes  
private static final int WARNING_TIME = 25 * 60 * 1000;

// Activity updates every 2 minutes
activityTimer = new Timer(2 * 60 * 1000, e -> updateActivity());
```

**Ready for quick session testing!** ⏱️🚀