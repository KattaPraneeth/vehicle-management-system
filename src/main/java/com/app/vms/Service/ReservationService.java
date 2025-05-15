package com.app.vms.Service;

import com.app.vms.entity.Reservation;
import com.app.vms.entity.User;
import com.app.vms.entity.Vehicle;
import com.app.vms.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ReservationRepository reservationRepository;

    public String reserveVehicle(Long vehicleId, Long userId, LocalDateTime pickupDate, LocalDateTime returnDate) {
        Optional<User> userOptional = userService.getById(userId);
        Optional<Vehicle> vehicleOptional = vehicleService.getById(vehicleId);

        if(userOptional.isPresent() && vehicleOptional.isPresent()){
            Vehicle vehicle = vehicleOptional.get();
            User user = userOptional.get();
            if(!vehicle.isAvailable()){
                return "Vehicle not available";
            }

            vehicle.setAvailable(false);
            vehicleService.addOrUpdateVehicle(vehicle);

            Reservation reservation = new Reservation();
            reservation.setVehicle(vehicle);
            reservation.setUser(user);
            reservation.setPickupDate(pickupDate);
            reservation.setReturnDate(returnDate);

            reservationRepository.save(reservation);
            return "Reservation successful!";
        }

        return "Vehicle or User not found!";
    }

    public List<Reservation> findByVehicle(Vehicle vehicle) {
        return reservationRepository.findByVehicle(vehicle);
    }

    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


}
