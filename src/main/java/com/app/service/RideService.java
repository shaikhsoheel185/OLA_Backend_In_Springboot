package com.app.service;

import com.app.exception.DriverException;
import com.app.exception.RideException;
import com.app.modal.Driver;
import com.app.modal.Ride;
import com.app.modal.User;
import com.app.request.RideRequest;
import org.springframework.stereotype.Service;

@Service
public interface RideService {

    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;

    public Ride createRideRequest(User user, Driver nearesDriver,
                                  double picupLatitude,double pickupLongitude,
                                  double destinationLatitude,double destinationLongitude,
                                  String pickupArea,String destinationArea);

    public void acceptRide(Integer rideId) throws RideException;

    public void declineRide(Integer rideId, Integer driverId) throws RideException;

    public void startRide(Integer rideId,int opt) throws RideException;

    public void completeRide(Integer rideId) throws RideException;

    public void cancleRide(Integer rideId) throws RideException;

    public Ride findRideById(Integer rideId) throws RideException;
}
