package service.implementation;

import dao.SimpleVehicleDao;
import model.ClientVehicle;
import model.SimpleVehicle;
import service.VehicleService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class VehicleServiceAdapter extends UnicastRemoteObject implements VehicleService {
    
    private SimpleVehicleDao vehicleDao;
    
    public VehicleServiceAdapter() throws RemoteException {
        super();
        this.vehicleDao = new SimpleVehicleDao();
    }
    
    @Override
    public boolean addVehicle(ClientVehicle vehicle) throws RemoteException {
        SimpleVehicle sv = toSimple(vehicle);
        if (sv.getPlateNumber() == null || sv.getPlateNumber().trim().isEmpty()) {
            throw new RemoteException("Plate number is required");
        }
        if (sv.getModel() == null || sv.getModel().trim().isEmpty()) {
            throw new RemoteException("Vehicle model is required");
        }
        if (sv.getPricePerDay() <= 0) {
            throw new RemoteException("Price per day must be greater than 0");
        }
        return vehicleDao.saveVehicle(sv);
    }
    
    @Override
    public boolean updateVehicle(ClientVehicle vehicle) throws RemoteException {
        SimpleVehicle sv = toSimple(vehicle);
        SimpleVehicle existingVehicle = vehicleDao.getVehicleById(sv.getId());
        if (existingVehicle == null) {
            throw new RemoteException("Vehicle not found");
        }
        return vehicleDao.updateVehicle(sv);
    }
    
    @Override
    public boolean deleteVehicle(int vehicleId) throws RemoteException {
        return vehicleDao.deleteVehicle(vehicleId);
    }
    
    @Override
    public ClientVehicle getVehicleById(int vehicleId) throws RemoteException {
        return toClient(vehicleDao.getVehicleById(vehicleId));
    }
    
    @Override
    public List<ClientVehicle> getAllVehicles() throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getAllVehicles();
        List<ClientVehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toClient(sv));
            }
        }
        return vehicles;
    }
    
    @Override
    public List<ClientVehicle> getAvailableVehicles() throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getAvailableVehicles();
        List<ClientVehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toClient(sv));
            }
        }
        return vehicles;
    }
    
    @Override
    public List<ClientVehicle> getVehiclesByCategory(String category) throws RemoteException {
        List<SimpleVehicle> simpleVehicles = vehicleDao.getVehiclesByCategory(category);
        List<ClientVehicle> vehicles = new ArrayList<>();
        if (simpleVehicles != null) {
            for (SimpleVehicle sv : simpleVehicles) {
                vehicles.add(toClient(sv));
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
    
    private SimpleVehicle toSimple(ClientVehicle v) {
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
    
    private ClientVehicle toClient(SimpleVehicle sv) {
        if (sv == null) return null;
        ClientVehicle v = new ClientVehicle(sv.getPlateNumber(), sv.getModel(), sv.getCategory(), sv.getPricePerDay());
        v.setId(sv.getId());
        v.setFuelType(sv.getFuelType());
        v.setTransmission(sv.getTransmission());
        v.setSeats(sv.getSeats());
        v.setImagePath(sv.getImagePath());
        v.setStatus(sv.getStatus());
        return v;
    }
}