package com.SoftwareDesign.BeautySalon.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BeautyService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @ManyToMany(mappedBy = "beautyServices")
    List<Appointment> appointments;

    public BeautyService(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public BeautyService(Long id, String name, BigDecimal price, EmployeeType employeeType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.employeeType = employeeType;
    }

    public BeautyService(String name, BigDecimal price, EmployeeType employeeType) {
        this.name = name;
        this.price = price;
        this.employeeType = employeeType;
    }
}
