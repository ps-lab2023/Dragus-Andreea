package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Employee {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employee")
    @ToString.Exclude
    private List<Appointment> appointments;


    public Employee(Long id, String name, UserType userType, String username, String password, EmployeeType employeeType) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.userName = username;
        this.password = password;
        this.employeeType = employeeType;
        this.appointments = new ArrayList<Appointment>();
    }

    public Employee( String name, UserType userType, String username, String password, EmployeeType employeeType) {
        this.name = name;
        this.userType = userType;
        this.userName = username;
        this.password = password;
        this.employeeType = employeeType;
        this.appointments = new ArrayList<Appointment>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
}
