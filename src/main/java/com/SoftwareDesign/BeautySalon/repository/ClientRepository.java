package com.SoftwareDesign.BeautySalon.repository;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomClientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, CustomClientRepository {
    List<Client> findAll();
    Optional<Client> findById(Long id);
    Optional<Client> findByName(String name);
    Optional<Client> findByUserName(String username);
    Optional<Client> findByAppointmentsIsContaining(Appointment appointment);
    List<Client> findAllByLoyaltyPointsGreaterThan(int points);
    void deleteById(Long id);
}
