package com.app.request;

import com.app.modal.License;
import com.app.modal.Vehicle;

import lombok.Data;

@Data
public class DriverSignupRequest {

    private String name;
    private String email;
    private String mobile;
    private String password;
    private double latitude;
    private double longitude;

    private License license;
    private Vehicle vehicle;
}
