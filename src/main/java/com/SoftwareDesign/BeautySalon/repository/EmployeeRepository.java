package com.SoftwareDesign.BeautySalon.repository;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomEmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, CustomEmployeeRepository {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Optional<Employee> findByName(String name);
    Optional<Employee> findByUserName(String username);
    Optional<Employee> findByAppointmentsIsContaining(Appointment appointment);
    List<Employee> findAllByEmployeeType(EmployeeType employeeType);
    void deleteById(Long id);


}
