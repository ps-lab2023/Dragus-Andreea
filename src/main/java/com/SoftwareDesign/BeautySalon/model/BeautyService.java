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
    private long id;
    private String name;
    private BigDecimal price;

    @ManyToMany(mappedBy = "beautyServices")
    List<Appointment> appointments;

    public BeautyService(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
