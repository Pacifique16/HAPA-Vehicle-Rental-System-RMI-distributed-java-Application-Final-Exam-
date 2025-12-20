package dao;

import model.SimpleVehicle;
import java.util.*;

public class SimpleVehicleDao {
    private static Map<Integer, SimpleVehicle> vehicles = new HashMap<>();
    private static int nextId = 1;
    
    static {
        // Add sample vehicles
        SimpleVehicle v1 = new SimpleVehicle("RAD 123A", "Toyota Prius", "Sedan", 50.00);
        v1.setId(nextId++);
        v1.setFuelType("Hybrid");
        v1.setTransmission("Automatic");
        v1.setSeats(5);
        v1.setStatus("Available");
        vehicles.put(v1.getId(), v1);
        
        SimpleVehicle v2 = new SimpleVehicle("RAD 456B", "Toyota RAV4", "SUV", 80.00);
        v2.setId(nextId++);
        v2.setFuelType("Petrol");
        v2.setTransmission("Automatic");
        v2.setSeats(7);
        v2.setStatus("Available");
        vehicles.put(v2.getId(), v2);
    }
    
    public boolean saveVehicle(SimpleVehicle vehicle) {
        vehicle.setId(nextId++);
        vehicles.put(vehicle.getId(), vehicle);
        return true;
    }
    
    public boolean updateVehicle(SimpleVehicle vehicle) {
        if (vehicles.containsKey(vehicle.getId())) {
            vehicles.put(vehicle.getId(), vehicle);
            return true;
        }
        return false;
    }
    
    public boolean deleteVehicle(int vehicleId) {
        return vehicles.remove(vehicleId) != null;
    }
    
    public SimpleVehicle getVehicleById(int vehicleId) {
        return vehicles.get(vehicleId);
    }
    
    public List<SimpleVehicle> getAllVehicles() {
        return new ArrayList<>(vehicles.values());
    }
    
    public List<SimpleVehicle> getAvailableVehicles() {
        return vehicles.values().stream()
                .filter(v -> "Available".equals(v.getStatus()))
                .collect(ArrayList::new, (list, vehicle) -> list.add(vehicle), ArrayList::addAll);
    }
    
    public List<SimpleVehicle> getVehiclesByCategory(String category) {
        return vehicles.values().stream()
                .filter(v -> v.getCategory().equals(category))
                .collect(ArrayList::new, (list, vehicle) -> list.add(vehicle), ArrayList::addAll);
    }
}