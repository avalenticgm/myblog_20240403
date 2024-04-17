package it.cgmconsulting.myblog.repository;


import it.cgmconsulting.myblog.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    Optional<Registration> findByConfirmCodeAndEndDateAfter(String confirmCode, LocalDateTime now);
}
