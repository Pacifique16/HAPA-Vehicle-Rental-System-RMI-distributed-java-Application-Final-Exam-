package dao;

import model.Vehicle;
import java.util.List;

public interface VehicleDAO {
    boolean saveVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(int id);
    boolean updateVehicle(Vehicle vehicle);
    boolean deleteVehicle(int id);
    List<Vehicle> getAvailableVehicles();
    List<Vehicle> searchVehicles(String query);
    boolean updateVehicleStatus(int vehicleId, String status);
    List<Vehicle> getVehiclesByCategory(String category);
    boolean isDuplicatePlateNumber(String plateNumber);
}