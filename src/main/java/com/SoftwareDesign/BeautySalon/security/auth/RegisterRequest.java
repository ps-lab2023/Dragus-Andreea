package com.SoftwareDesign.BeautySalon.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String userName;
    private String password;
    private String userType;
    private String employeeType;
}
