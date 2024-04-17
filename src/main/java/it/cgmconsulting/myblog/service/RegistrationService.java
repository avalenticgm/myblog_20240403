package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Registration;
import it.cgmconsulting.myblog.exception.GenericException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public void save(Registration registration){
        registrationRepository.save(registration);
    }

    public Registration isValidRegistrationToken(String confirmCode){
        return registrationRepository.findByConfirmCodeAndEndDateAfter(confirmCode, LocalDateTime.now())
                .orElseThrow(() -> new GenericException("Token expired"));
    }
}
