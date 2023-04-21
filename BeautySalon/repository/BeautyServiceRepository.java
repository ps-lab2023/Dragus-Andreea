package com.SoftwareDesign.BeautySalon.repository;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeautyServiceRepository extends JpaRepository<BeautyService, Long> {
    Optional<BeautyService> findBeautyServiceById(Long id);
    Optional<BeautyService> findBeautyServiceByName(String name);
    List<BeautyService> findBeautyServicesByEmployeeType(EmployeeType employeeType);

}
