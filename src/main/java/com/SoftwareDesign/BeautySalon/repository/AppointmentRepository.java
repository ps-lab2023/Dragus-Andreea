package com.SoftwareDesign.BeautySalon.repository;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomAppointmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, CustomAppointmentRepository {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    Optional<Appointment> findByEmployeeNameAndDateTime(String name, LocalDateTime dateTime);
    Optional<Appointment> findByClientNameAndDateTime(String name, LocalDateTime dateTime);
    List<Appointment> findAllByEmployeeId(Long id);
    List<Appointment> findAllByClientId(Long id);
    List<Appointment> findAllByBeautyServicesContaining(BeautyService beautyService);
    List<Appointment> findAllByTotalPriceGreaterThan(BigDecimal totalPrice);
    void deleteById(Long id);

}
