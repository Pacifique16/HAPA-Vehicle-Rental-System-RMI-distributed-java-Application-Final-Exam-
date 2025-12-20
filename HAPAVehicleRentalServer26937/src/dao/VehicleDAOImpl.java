package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Vehicle;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id"));
                v.setPlateNumber(rs.getString("plate_number"));
                v.setModel(rs.getString("model"));
                v.setCategory(rs.getString("category"));
                v.setPricePerDay(rs.getDouble("price_per_day"));
                v.setImagePath(rs.getString("image_path"));
                v.setFuelType(rs.getString("fuel_type"));
                v.setTransmission(rs.getString("transmission"));
                v.setSeats(rs.getInt("seats"));
                v.setStatus(rs.getString("status"));
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public int countVehicles() {
        String sql = "SELECT COUNT(*) FROM vehicles";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public List<Vehicle> searchVehicles(String query) {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE model ILIKE ? OR category ILIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, "%" + query + "%");
            pst.setString(2, "%" + query + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id"));
                v.setPlateNumber(rs.getString("plate_number"));
                v.setModel(rs.getString("model"));
                v.setCategory(rs.getString("category"));
                v.setPricePerDay(rs.getDouble("price_per_day"));
                v.setImagePath(rs.getString("image_path"));
                v.setFuelType(rs.getString("fuel_type"));
                v.setTransmission(rs.getString("transmission"));
                v.setSeats(rs.getInt("seats"));
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public Vehicle findById(int id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id"));
                v.setPlateNumber(rs.getString("plate_number"));
                v.setModel(rs.getString("model"));
                v.setCategory(rs.getString("category"));
                v.setPricePerDay(rs.getDouble("price_per_day"));
                v.setImagePath(rs.getString("image_path"));
                v.setFuelType(rs.getString("fuel_type"));
                v.setTransmission(rs.getString("transmission"));
                v.setSeats(rs.getInt("seats"));
                return v;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (plate_number, model, category, price_per_day, image_path, fuel_type, transmission, seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, vehicle.getPlateNumber());
            pst.setString(2, vehicle.getModel());
            pst.setString(3, vehicle.getCategory());
            pst.setDouble(4, vehicle.getPricePerDay());
            pst.setString(5, vehicle.getImagePath());
            pst.setString(6, vehicle.getFuelType());
            pst.setString(7, vehicle.getTransmission());
            pst.setInt(8, vehicle.getSeats());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET plate_number = ?, model = ?, category = ?, price_per_day = ?, image_path = ?, fuel_type = ?, transmission = ?, seats = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, vehicle.getPlateNumber());
            pst.setString(2, vehicle.getModel());
            pst.setString(3, vehicle.getCategory());
            pst.setDouble(4, vehicle.getPricePerDay());
            pst.setString(5, vehicle.getImagePath());
            pst.setString(6, vehicle.getFuelType());
            pst.setString(7, vehicle.getTransmission());
            pst.setInt(8, vehicle.getSeats());
            pst.setInt(9, vehicle.getId());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countAvailableToday() {
        String sql = "SELECT COUNT(*) FROM vehicles v " +
            "WHERE v.status != 'Maintenance' " +
            "AND v.id NOT IN ( " +
            "   SELECT vehicle_id FROM bookings " +
            "   WHERE status NOT IN ('CANCELLED', 'REJECTED', 'EXPIRED') " +
            "   AND CURRENT_DATE BETWEEN start_date AND end_date " +
            ")";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public boolean updateVehicleStatus(int vehicleId, String status) {
        String sql = "UPDATE vehicles SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, vehicleId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean isVehicleAvailableForBooking(int vehicleId) {
        String sql = "SELECT status FROM vehicles WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, vehicleId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                return !"Maintenance".equals(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}