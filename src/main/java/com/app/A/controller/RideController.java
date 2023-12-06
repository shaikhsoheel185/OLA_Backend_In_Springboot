package com.app.A.controller;

import com.app.Dtos.RideDTO;
import com.app.Dtos.mapper.DtoMapper;
import com.app.exception.DriverException;
import com.app.exception.RideException;
import com.app.exception.UserException;
import com.app.modal.Driver;
import com.app.modal.Ride;
import com.app.modal.User;
import com.app.request.RideRequest;
import com.app.request.StartRideRequest;
import com.app.response.MessageResponse;
import com.app.service.DriverService;
import com.app.service.RideService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideService rideService;
    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;


    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization") String jwt) throws UserException, DriverException, UserException, DriverException, UserException, DriverException {

        User user =userService.getReqUserProfile(jwt);

        Ride ride=rideService.requestRide(rideRequest, user);

        RideDTO rideDto= DtoMapper.toRideDto(ride);

        return new ResponseEntity<>(rideDto, HttpStatus.ACCEPTED);
    }


    @PutMapping("/{rideId}/accept")
    public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId) throws UserException, RideException, RideException {

        rideService.acceptRide(rideId);

        MessageResponse res=new MessageResponse("Ride Accepted By Driver");

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/decline")
    public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer rideId)
            throws UserException, RideException, DriverException{

        Driver driver = driverService.getReqDriverProfile(jwt);

        rideService.declineRide(rideId, driver.getId());

        MessageResponse res=new MessageResponse("Ride decline By Driver");

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/start")
    public ResponseEntity<MessageResponse> rideStartHandler(@PathVariable Integer rideId, @RequestBody StartRideRequest req) throws UserException, RideException{

        rideService.startRide(rideId,req.getOtp());

        MessageResponse res=new MessageResponse("Ride is started");

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<MessageResponse> rideCompleteHandler(@PathVariable Integer rideId) throws UserException, RideException{

        rideService.completeRide(rideId);

        MessageResponse res=new MessageResponse("Ride Is Completed Thank You For Booking Cab");

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideDTO> findRideByIdHandler(@PathVariable Integer rideId, @RequestHeader("Authorization") String jwt) throws UserException, RideException{

        User user =userService.getReqUserProfile(jwt);

        Ride ride =rideService.findRideById(rideId);


        RideDTO rideDto=DtoMapper.toRideDto(ride);


        return new ResponseEntity<RideDTO>(rideDto,HttpStatus.ACCEPTED);
    }
}
