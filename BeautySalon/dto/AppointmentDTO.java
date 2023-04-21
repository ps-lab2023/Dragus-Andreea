package com.SoftwareDesign.BeautySalon.dto;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private Long id;
    private ClientDTO clientDTO;
    private EmployeeDTO employeeDTO;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private List<BeautyServiceDTO> beautyServicesDTO;
    private BigDecimal totalPrice = new BigDecimal(0);

}
