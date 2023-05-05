package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Client implements UserDetails {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String userName;
    private String password;
    private int loyaltyPoints;
    private String salesCode;
    private boolean loggedIn;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<Appointment>();

    public Client(Long id, String name, String userName, String password, int loyaltyPoints) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.loyaltyPoints = loyaltyPoints;

        this.userType = UserType.CLIENT;
    }

    public Client(String name, String userName, String password, int loyaltyPoints) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.loyaltyPoints = loyaltyPoints;

        this.userType = UserType.CLIENT;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        loyaltyPoints += appointment.getTotalPrice().intValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserName() {
        return userName;
    }
}
