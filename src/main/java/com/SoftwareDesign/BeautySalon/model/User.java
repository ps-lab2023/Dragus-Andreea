package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@Email()
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String userName;
    private String password;

    public User(String name, UserType userType, String userName, String password) {
        this.name = name;
        this.userType = userType;
        this.userName = userName;
        this.password = password;
    }
}
