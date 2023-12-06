package com.app.service;

import com.app.exception.DriverException;
import com.app.modal.Driver;
import com.app.modal.Ride;
import com.app.request.DriverSignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DriverService {

    public Driver registerDriver(DriverSignupRequest driverSignupRequest);

    public List<Driver> getAvailableDrivers(double pickupLatitude,
                                            double picupLongitude,  Ride ride);

    public Driver findNearestDriver(List<Driver> availableDrivers,
                                    double picupLatitude, double picupLongitude);

    public Driver getReqDriverProfile(String jwt) throws DriverException;

    public Ride getDriversCurrentRide(Integer driverId) throws DriverException;

    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;

    public Driver findDriverById(Integer driverId) throws DriverException;

    public List<Ride> completedRids(Integer driverId) throws DriverException;
}
