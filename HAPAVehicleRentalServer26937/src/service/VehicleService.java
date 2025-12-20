package service;

import model.Vehicle;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VehicleService extends Remote {
    boolean addVehicle(Vehicle vehicle) throws RemoteException;
    boolean updateVehicle(Vehicle vehicle) throws RemoteException;
    boolean deleteVehicle(int id) throws RemoteException;
    Vehicle findById(int id) throws RemoteException;
    List<Vehicle> getAllVehicles() throws RemoteException;
    List<Vehicle> getAvailableVehicles() throws RemoteException;
    boolean updateVehicleStatus(int vehicleId, String status) throws RemoteException;
    int countVehicles() throws RemoteException;
    List<Vehicle> searchVehicles(String keyword) throws RemoteException;
}