package service.implementation;

import dao.SimpleVehicleDao;
import model.Vehicle;
import model.SimpleVehicle;
import service.VehicleService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class SimpleVehicleService extends UnicastRemoteObject implements VehicleService {
    
    private SimpleVehicleDao vehicleDao;
    
    public SimpleVehicleService() throws RemoteException {
        super();
        this.vehicleDao = new SimpleVehicleDao();
    }
    
    @Override
    public boolean addVehicle(Vehicle vehicle) throws RemoteException {
        SimpleVehicle sv = toSimple(vehicle);
        return vehicleDao.saveVehicle(sv);
    }
    
    @Override
    public boolean updateVehicle(Vehicle vehicle) throws RemoteException {
        SimpleVehicle sv = toSimple(vehicle);
        return vehicleDao.updateVehicle(sv);
    }
    
    @Override
    public boolean deleteVehicle(int vehicleId) throws RemoteException {
        return vehicleDao.deleteVehicle(vehicleId);
    }
    
    @Override
    public Vehicle getVehicleById(int vehicleId) throws RemoteException {
        return toVehicle(vehicleDao.getVehicleById(vehicleId));
    }
    
    @Override
    public List<Vehicle> getAllVehicles() throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getAllVehicles();
        List<Vehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toVehicle(sv));
            }
        }
        return vehicles;
    }
    
    @Override
    public List<Vehicle> getAvailableVehicles() throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getAvailableVehicles();
        List<Vehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toVehicle(sv));
            }
        }
        return vehicles;
    }
    
    @Override
    public List<Vehicle> getVehiclesByCategory(String category) throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getVehiclesByCategory(category);
        List<Vehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toVehicle(sv));
            }
        }
        return vehicles;
    }
    
    @Override
    public boolean updateVehicleStatus(int vehicleId, String status) throws RemoteException {
        SimpleVehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if (vehicle != null) {
            vehicle.setStatus(status);
            return vehicleDao.updateVehicle(vehicle);
        }
        return false;
    }
    
    private SimpleVehicle toSimple(Vehicle v) {
        if (v == null) return null;
        SimpleVehicle sv = new SimpleVehicle(v.getPlateNumber(), v.getModel(), v.getCategory(), v.getPricePerDay());
        sv.setId(v.getId());
        sv.setFuelType(v.getFuelType());
        sv.setTransmission(v.getTransmission());
        sv.setSeats(v.getSeats());
        sv.setImagePath(v.getImagePath());
        sv.setStatus(v.getStatus());
        return sv;
    }
    
    private Vehicle toVehicle(SimpleVehicle sv) {
        if (sv == null) return null;
        Vehicle v = new Vehicle(sv.getPlateNumber(), sv.getModel(), sv.getCategory(), sv.getPricePerDay());
        v.setId(sv.getId());
        v.setFuelType(sv.getFuelType());
        v.setTransmission(sv.getTransmission());
        v.setSeats(sv.getSeats());
        v.setImagePath(sv.getImagePath());
        v.setStatus(sv.getStatus());
        return v;
    }
}