# HAPA Vehicle Rental System - RMI Transformation Guide

## Overview
Transform your existing HAPA_Vehicle_Rental_System_finalExam into a distributed client-server architecture using RMI while keeping your beautiful UI design.

## Current Status
✅ HapaVehicleRentalClient26937 - Created (copy of your original project)
✅ HAPAVehicleRentalServer26937 - Server structure created
✅ Server has: Simple in-memory DAOs, RMI services, models

## What You Need To Do

### Step 1: Update Client LoginForm to use RMI

**File**: `HapaVehicleRentalClient26937/src/view/LoginForm.java`

**Change this:**
```java
private UserDAO userDAO = new UserDAOImpl();
```

**To this:**
```java
private UserService userService;

// In constructor, add:
try {
    userService = (UserService) Naming.lookup("rmi://localhost:3500/UserService");
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
}
```

**Change login method from:**
```java
User user = userDAO.login(username, password);
```

**To:**
```java
User user = userService.authenticateUser(username, password);
```

### Step 2: Create RMI Service Interfaces in Client

**File**: `HapaVehicleRentalClient26937/src/service/UserService.java`
```java
package service;

import model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    User authenticateUser(String username, String password) throws RemoteException;
    boolean addUser(User user) throws RemoteException;
    boolean updateUser(User user) throws RemoteException;
    boolean deleteUser(int id) throws RemoteException;
    User findById(int id) throws RemoteException;
    List<User> getAllUsers() throws RemoteException;
    boolean changePassword(int id, String oldP, String newP) throws RemoteException;
    boolean updateUserProfile(User user) throws RemoteException;
    int countUsers() throws RemoteException;
    boolean updateUserStatus(int userId, String status) throws RemoteException;
}
```

### Step 3: Create Similar Interfaces for Vehicle and Booking Services

Follow the same pattern for VehicleService and BookingService.

### Step 4: Update Server to Use Real Database DAOs

**Copy these files from client to server:**
- `HapaVehicleRentalClient26937/src/dao/UserDAOImpl.java` → `HAPAVehicleRentalServer26937/src/dao/`
- `HapaVehicleRentalClient26937/src/dao/VehicleDAOImpl.java` → `HAPAVehicleRentalServer26937/src/dao/`
- `HapaVehicleRentalClient26937/src/dao/BookingDAOImpl.java` → `HAPAVehicleRentalServer26937/src/dao/`

### Step 5: Create RMI Service Implementations in Server

**File**: `HAPAVehicleRentalServer26937/src/service/implementation/UserServiceImpl.java`
```java
package service.implementation;

import dao.UserDAOImpl;
import model.User;
import service.UserService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    
    private UserDAOImpl userDAO;
    
    public UserServiceImpl() throws RemoteException {
        super();
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User authenticateUser(String username, String password) throws RemoteException {
        return userDAO.login(username, password);
    }
    
    @Override
    public boolean addUser(User user) throws RemoteException {
        return userDAO.addUser(user);
    }
    
    // Implement all other methods similarly...
}
```

### Step 6: Update Server.java

**File**: `HAPAVehicleRentalServer26937/src/controller/Server.java`
```java
UserServiceImpl userService = new UserServiceImpl();
VehicleServiceImpl vehicleService = new VehicleServiceImpl();
BookingServiceImpl bookingService = new BookingServiceImpl();

Naming.rebind("rmi://localhost:3500/UserService", userService);
Naming.rebind("rmi://localhost:3500/VehicleService", vehicleService);
Naming.rebind("rmi://localhost:3500/BookingService", bookingService);
```

### Step 7: Update All Client Views

Update these files to use RMI services instead of DAOs:
- `AdminDashboard.java`
- `CustomerDashboard.java`
- `SignupForm.java`
- `ManageUsersPanel.java`
- `ManageVehiclesPanel.java`
- `BookingsApprovalPanel.java`
- `BookVehiclePanel.java`
- `MyBookingsPanel.java`

**Pattern:**
```java
// OLD:
private UserDAO userDAO = new UserDAOImpl();
userDAO.addUser(user);

// NEW:
private UserService userService;
// Initialize in constructor with RMI lookup
userService.addUser(user);
```

### Step 8: Remove DAO Package from Client

After updating all views, delete:
- `HapaVehicleRentalClient26937/src/dao/` (entire folder)

Keep only:
- `src/model/` - POJOs without JPA
- `src/service/` - RMI interfaces
- `src/view/` - Your beautiful UI
- `src/util/` - Validators
- `images/` - All your images

## Testing Steps

1. **Start Server**: Run `Server.java` in HAPAVehicleRentalServer26937
2. **Start Client**: Run `LoginForm.java` in HapaVehicleRentalClient26937
3. **Test Login**: Use your existing credentials
4. **Test All Features**: CRUD operations, bookings, reports

## Key Points

- ✅ Keep ALL your UI design - no changes needed to .form files
- ✅ Keep ALL your images and resources
- ✅ Server handles all database operations
- ✅ Client only handles UI and RMI calls
- ✅ Same database, same data, same beautiful design!

## Quick Reference

**Server Side Has:**
- DAO implementations (database access)
- Service implementations (RMI)
- Models (can have JPA if using Hibernate)
- DBConnection

**Client Side Has:**
- View package (all your UI)
- Service interfaces (RMI)
- Models (POJOs, no JPA)
- Images and resources
- Util package (validators)

Your system will work exactly the same, but now distributed!