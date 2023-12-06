package com.app.response;

import com.app.domain.UserRole;
import lombok.Data;

@Data
public class JwtResponse {

    private String jwt;
    private String message;
    private boolean isAuthenticated;
    private boolean isError;
    private String errorDetails;
    private UserRole type;

}
