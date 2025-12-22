package util;

import java.util.regex.Pattern;
import java.util.Date;

/**
 * Comprehensive Validation Utility - Contains both Technical and Business validations
 */
public class ValidationUtil {
    
    // ========== TECHNICAL VALIDATIONS (Format & Data Type) ==========
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Phone validation pattern (supports various formats)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    // Username validation (alphanumeric, underscore, 3-20 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    // Plate number validation (letters and numbers)
    private static final Pattern PLATE_PATTERN = Pattern.compile(
        "^[A-Z0-9]{3,10}$"
    );
    
    // Email format validation
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    // Phone format validation
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("\\s|-", "")).matches();
    }
    
    // Username format validation
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    // Password format validation (minimum 6 characters)
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    // Full name format validation (contains only letters and spaces)
    public static boolean isValidFullName(String fullName) {
        return fullName != null && fullName.trim().length() >= 2 && 
               fullName.matches("^[a-zA-Z\\s]+$");
    }
    
    // Plate number format validation
    public static boolean isValidPlateNumber(String plateNumber) {
        return plateNumber != null && PLATE_PATTERN.matcher(plateNumber.toUpperCase()).matches();
    }
    
    // Generic string validation (not null or empty)
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    // Numeric validation (positive integer)
    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }
    
    
    
    
    
    
    
    
    
    
    // ========== BUSINESS VALIDATIONS (Domain Logic & Rules) ==========
    
    
    
    
    
    // Date validation (not in the past for bookings)
    public static boolean isValidFutureDate(Date date) {
        return date != null && date.after(new Date());
    }
    
    // Date range validation (end date after start date)
    public static boolean isValidDateRange(Date startDate, Date endDate) {
        return startDate != null && endDate != null && endDate.after(startDate);
    }
    
    // Role validation (business-defined roles)
    public static boolean isValidRole(String role) {
        return role != null && (role.equals("ADMIN") || role.equals("CUSTOMER"));
    }
    
    // Status validation for users (business-defined statuses)
    public static boolean isValidUserStatus(String status) {
        return status != null && (status.equals("Active") || status.equals("Inactive"));
    }
    
    // Status validation for vehicles (business-defined statuses)
    public static boolean isValidVehicleStatus(String status) {
        return status != null && (status.equals("Available") || status.equals("Rented") || 
                                 status.equals("Maintenance"));
    }
    
    // Status validation for bookings (business-defined statuses)
    public static boolean isValidBookingStatus(String status) {
        return status != null && (status.equals("PENDING") || status.equals("APPROVED") || 
                                 status.equals("REJECTED") || status.equals("COMPLETED"));
    }
    
    // Seats validation (business rule: reasonable range for vehicles)
    public static boolean isValidSeats(int seats) {
        return seats > 0 && seats <= 50;
    }
    
    // Price validation (business rule: must be positive)
    public static boolean isValidPrice(double price) {
        return price > 0;
    }
    
    // Category validation (business-defined categories)
    public static boolean isValidCategory(String category) {
        return category != null && category.trim().length() >= 2;
    }
    
    // Fuel type validation (business-defined fuel types)
    public static boolean isValidFuelType(String fuelType) {
        return fuelType != null && (fuelType.equals("Petrol") || fuelType.equals("Diesel") || 
                                   fuelType.equals("Electric") || fuelType.equals("Hybrid"));
    }
    
    // Transmission validation (business-defined transmission types)
    public static boolean isValidTransmission(String transmission) {
        return transmission != null && (transmission.equals("Manual") || transmission.equals("Automatic"));
    }
    
    // Vehicle model validation (business rule: minimum length)
    public static boolean isValidVehicleModel(String model) {
        return model != null && model.trim().length() >= 2;
    }
    
    // Booking duration validation (business rule: minimum 1 day)
    public static boolean isValidBookingDuration(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return false;
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        return diffInDays >= 1;
    }
    
    // Age validation for customers (business rule: minimum age 18)
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 100;
    }
    
    // Vehicle availability check (business logic)
    public static boolean isVehicleAvailable(String status) {
        return "Available".equals(status);
    }
    
    // User account active check (business logic)
    public static boolean isUserActive(String status) {
        return "Active".equals(status);
    }
}