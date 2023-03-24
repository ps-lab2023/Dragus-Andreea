package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDateTime dateTime;

    @ManyToMany
    @JoinTable(
            name = "appointment_beauty_service",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "beauty_service_id"))
    @ToString.Exclude
    private List<BeautyService> beautyServices;

    private BigDecimal totalPrice;

    public Appointment(Long id, Client client, Employee employee, LocalDateTime dateTime) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.dateTime = dateTime;
        this.beautyServices = new ArrayList<BeautyService>();
        totalPrice = new BigDecimal(0);
    }

    public Appointment( Client client, Employee employee, LocalDateTime dateTime) {
        this.client = client;
        this.employee = employee;
        this.dateTime = dateTime;
        this.beautyServices = new ArrayList<BeautyService>();
        totalPrice = new BigDecimal(0);
    }

    public void computeTotalPrice() {
        for(BeautyService bs: beautyServices) {
            totalPrice = totalPrice.add(bs.getPrice());
        }
    }

    public void addBeautyService(BeautyService beautyService) {
        beautyServices.add(beautyService);
        totalPrice = totalPrice.add(beautyService.getPrice());
    }
}
