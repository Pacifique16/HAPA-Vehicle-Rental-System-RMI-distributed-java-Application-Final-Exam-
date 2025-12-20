package dao;

import model.Vehicle;
import java.util.List;

public interface VehicleDAO {
    List<Vehicle> getAllVehicles();
    int countVehicles();
    List<Vehicle> searchVehicles(String query);
    Vehicle findById(int id);
    boolean deleteVehicle(int id);
    boolean addVehicle(Vehicle vehicle);
    boolean updateVehicle(Vehicle vehicle);
    int countAvailableToday();
    boolean updateVehicleStatus(int vehicleId, String status);
    boolean isVehicleAvailableForBooking(int vehicleId);
}