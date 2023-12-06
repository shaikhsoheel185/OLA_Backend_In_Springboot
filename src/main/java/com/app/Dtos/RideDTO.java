package com.app.Dtos;

import com.app.modal.PaymentDetails;
import com.app.modal.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RideDTO {
    private Integer id;
    private UserDTO user;
    private DriverDTO driver;
    private double pickupLatitude;
    private double pickupLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private String pickupArea;
    private String destinationArea;
    private double distance;
    private long duration;
    private RideStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double fare;
    private PaymentDetails paymentDetails;
    private int otp;
}
