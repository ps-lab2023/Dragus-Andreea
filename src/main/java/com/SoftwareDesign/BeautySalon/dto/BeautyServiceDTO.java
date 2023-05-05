package com.SoftwareDesign.BeautySalon.dto;

import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeautyServiceDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private EmployeeType employeeType;
}
