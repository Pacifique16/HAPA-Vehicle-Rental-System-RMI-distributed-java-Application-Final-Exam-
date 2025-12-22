# HAPA Vehicle Rental System - Validation Rules Documentation

## Business Validation Rules (5 Required)

1. **Booking Date Validation**
   - Start date must be today or future date
   - End date must be after start date
   - Location: BookingService.java

2. **Vehicle Availability Validation**
   - Vehicle must be "Available" status to be booked
   - Cannot book already reserved vehicles
   - Location: BookingService.java

3. **User Role Authorization**
   - Only customers can make bookings
   - Only admins can manage vehicles and users
   - Location: LoginForm.java, AdminDashboard.java

4. **Booking Duration Validation**
   - Minimum booking duration: 1 day
   - Maximum booking duration: 30 days
   - Location: BookingForm.java

5. **Vehicle Category Pricing**
   - Economy vehicles: $30-50/day
   - Luxury vehicles: $100-300/day
   - SUV vehicles: $60-120/day
   - Location: VehicleService.java

## Technical Validation Rules (5 Required)

1. **Username Uniqueness**
   - Username must be unique in database
   - No duplicate usernames allowed
   - Location: UserDAOImpl.java

2. **Email Format Validation**
   - Must contain @ symbol and valid domain
   - Email format: user@domain.com
   - Location: SignupForm.java

3. **Password Strength**
   - Minimum 6 characters required
   - Must contain letters and numbers
   - Location: PasswordValidator.java

4. **Phone Number Format**
   - Must be 10 digits
   - Format: +250XXXXXXXXX
   - Location: SignupForm.java

5. **OTP Validation**
   - Must be exactly 5 digits
   - Expires after 5 minutes
   - Location: OTPDialog.java, OTPServiceImpl.java

## Implementation Status: ✅ ALL IMPLEMENTED