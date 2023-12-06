package com.app.service;

import com.app.exception.DriverException;
import com.app.exception.RideException;
import com.app.modal.Driver;
import com.app.modal.Ride;
import com.app.modal.RideStatus;
import com.app.modal.User;
import com.app.repository.DriverRepository;
import com.app.repository.RideRepository;
import com.app.request.RideRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
@Repository
public class RideServiceImpl implements RideService {


    @Autowired
    private DriverService driverService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private Calculators calculators;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {
        double picupLatitude=rideRequest.getPickupLatitude();
        double picupLongitude=rideRequest.getPickupLongitude();
        double destinationLatitude=rideRequest.getDestinationLatitude();
        double destinationLongitude=rideRequest.getDestinationLongitude();
        String pickupArea=rideRequest.getPickupArea();
        String destinationArea=rideRequest.getDestinationArea();

        Ride existingRide = new Ride();

        List<Driver> availableDrivers=driverService.getAvailableDrivers(picupLatitude,
                picupLongitude, existingRide);

        Driver nearestDriver=driverService.findNearestDriver(availableDrivers, picupLatitude, picupLongitude);

        if(nearestDriver==null) {
            throw new DriverException("Driver not available");
        }

        System.out.println(" duration ----- before ride ");

        Ride ride = createRideRequest(user, nearestDriver,
                picupLatitude, picupLongitude,
                destinationLatitude, destinationLongitude,
                pickupArea,destinationArea
        );

        return ride;
    }

    @Override
    public Ride createRideRequest(User user, Driver nearesDriver, double pickupLatitude, double pickupLongitude, double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea) {
        Ride ride=new Ride();

        ride.setDriver(nearesDriver);
        ride.setUser(user);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDestinationLatitude(destinationLatitude);
        ride.setDestinationLongitude(destinationLongitude);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPickupArea(pickupArea);
        ride.setDestinationArea(destinationArea);

        System.out.println(" ----- a - " + pickupLatitude);

        return rideRepository.save(ride);
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {


        Ride ride=findRideById(rideId);

        ride.setStatus(RideStatus.ACCEPTED);

        Driver driver = ride.getDriver();

        driver.setCurrentRide(ride);

        Random random = new Random();

        int otp = random.nextInt(9000) + 1000;
        ride.setOtp(otp);

        driverRepository.save(driver);

        rideRepository.save(ride);

    }

    @Override
    public void declineRide(Integer rideId, Integer driverId) throws RideException {

        Ride ride =findRideById(rideId);
        System.out.println(ride.getId());

        ride.getDeclinedDrivers().add(driverId);

        System.out.println(ride.getId()+" - "+ride.getDeclinedDrivers());

        List<Driver> availableDrivers=driverService.getAvailableDrivers(ride.getPickupLatitude(),
                ride.getPickupLongitude(), ride);

        Driver nearestDriver=driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(),
                ride.getPickupLongitude());


        ride.setDriver(nearestDriver);

        rideRepository.save(ride);


    }

    @Override
    public void startRide(Integer rideId, int opt) throws RideException {

        Ride ride=findRideById(rideId);

        if(opt!=ride.getOtp()) {
            throw new RideException("please provide a valid otp");
        }

        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);

    }

    @Override
    public void completeRide(Integer rideId) throws RideException {

        Ride ride=findRideById(rideId);

        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());;

        double distence=calculators.calculateDistance(ride.getDestinationLatitude(), ride.getDestinationLongitude(), ride.getPickupLatitude(), ride.getPickupLongitude());

        LocalDateTime start=ride.getStartTime();
        LocalDateTime end=ride.getEndTime();
        Duration duration = Duration.between(start, end);
        long milliSecond = duration.toMillis();


        System.out.println("duration ------- "+ milliSecond);
        double fare=calculators.calculateFare(distence);

        ride.setDistence(Math.round(distence * 100.0) / 100.0);
        ride.setFare((int) Math.round(fare));
        ride.setDuration(milliSecond);
        ride.setEndTime(LocalDateTime.now());


        Driver driver =ride.getDriver();
        driver.getRides().add(ride);
        driver.setCurrentRide(null);

        Integer driversRevenue=(int) (driver.getTotalRevenue()+ Math.round(fare*0.8)) ;
        driver.setTotalRevenue(driversRevenue);

        System.out.println("drivers revenue -- "+driversRevenue);

        driverRepository.save(driver);
        rideRepository.save(ride);

    }

    @Override
    public void cancleRide(Integer rideId) throws RideException {

        Ride ride=findRideById(rideId);
        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);

    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {
        Optional<Ride> ride=rideRepository.findById(rideId);

        if(ride.isPresent()) {
            return ride.get();
        }
        throw new RideException("ride not exist with id "+rideId);
    }
}
