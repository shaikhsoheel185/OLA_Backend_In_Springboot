package com.app.request;

import lombok.Data;

@Data
public class RideRequest {

    private double pickupLongitude;
    private double pickupLatitude;
    private double destinationLongitude;
    private double destinationLatitude;
    private String pickupArea;
    private String destinationArea;
}
