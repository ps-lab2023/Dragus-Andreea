package com.SoftwareDesign.BeautySalon.repository;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByName(String name);
    Optional<User> findByUserName(String username);
    Optional<User> findByUserNameAndPassword(String username, String password);
    List<User> findAllByUserType(UserType userType);
    void deleteById(Long id);

}
