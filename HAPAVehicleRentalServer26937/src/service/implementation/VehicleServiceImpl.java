package service.implementation;

import dao.VehicleDAOImpl;
import model.Vehicle;
import service.VehicleService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VehicleServiceImpl extends UnicastRemoteObject implements VehicleService {
    
    private VehicleDAOImpl vehicleDAO;
    
    public VehicleServiceImpl() throws RemoteException {
        super();
        this.vehicleDAO = new VehicleDAOImpl();
    }
    
    @Override
    public boolean addVehicle(Vehicle vehicle) throws RemoteException {
        return vehicleDAO.addVehicle(vehicle);
    }
    
    @Override
    public boolean updateVehicle(Vehicle vehicle) throws RemoteException {
        return vehicleDAO.updateVehicle(vehicle);
    }
    
    @Override
    public boolean deleteVehicle(int id) throws RemoteException {
        return vehicleDAO.deleteVehicle(id);
    }
    
    @Override
    public Vehicle findById(int id) throws RemoteException {
        return vehicleDAO.findById(id);
    }
    
    @Override
    public List<Vehicle> getAllVehicles() throws RemoteException {
        return vehicleDAO.getAllVehicles();
    }
    
    @Override
    public List<Vehicle> getAvailableVehicles() throws RemoteException {
        return vehicleDAO.getAllVehicles(); // Simplified - return all for now
    }
    
    @Override
    public boolean updateVehicleStatus(int vehicleId, String status) throws RemoteException {
        return vehicleDAO.updateVehicleStatus(vehicleId, status);
    }
    
    @Override
    public int countVehicles() throws RemoteException {
        return vehicleDAO.countVehicles();
    }
    
    @Override
    public List<Vehicle> searchVehicles(String keyword) throws RemoteException {
        return vehicleDAO.searchVehicles(keyword);
    }
}