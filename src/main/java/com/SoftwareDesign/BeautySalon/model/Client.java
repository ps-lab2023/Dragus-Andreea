package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Client {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String userName;
    private String password;

    private int loyaltyPoints;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
    @ToString.Exclude
    private List<Appointment> appointments;

    public Client(Long id, String name, String userName, String password, int loyaltyPoints) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.loyaltyPoints = loyaltyPoints;

        this.userType = UserType.CLIENT;
        appointments = new ArrayList<Appointment>();
    }

    public Client(String name, String userName, String password, int loyaltyPoints) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.loyaltyPoints = loyaltyPoints;

        this.userType = UserType.CLIENT;
        appointments = new ArrayList<Appointment>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        loyaltyPoints += appointment.getTotalPrice().intValue();
    }

}
