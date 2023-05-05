package com.SoftwareDesign.BeautySalon.dto;

import com.SoftwareDesign.BeautySalon.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String name;
    private UserType userType;
    private String userName;
    private String password;
    private boolean loggedIn;
}
