package com.app.Dtos;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LicenseDTO {

    private String number;
    private String expiryDate;
}
