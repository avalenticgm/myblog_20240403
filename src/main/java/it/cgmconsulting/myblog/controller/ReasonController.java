package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.ReasonService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Validated
public class ReasonController {

    private final ReasonService reasonService;

    //  serve esclusivamente per l'inserimento di una nuova reason
    @PostMapping("/v1/reasons")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> addReason(
            @RequestParam @NotBlank @Size(max = 30, min = 5) String reason,
            @RequestParam @NotNull LocalDate startDate,
            @RequestParam @Min(1) int severity){
        String msg = reasonService.addReason(reason, startDate, severity);
        if(msg != null)
            return new ResponseEntity<>(msg, HttpStatus.CREATED);

        return new ResponseEntity<>("Reason "+reason+" already present", HttpStatus.CONFLICT);
    }

    // cancellazione logica di una reason, ovvero invalidare la reason settando una data di fine validità
    @DeleteMapping("/v1/reasons")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> deleteReason(
            @RequestParam @NotBlank @Size(max = 30, min = 5) String reason){
            LocalDate now = LocalDate.now();
            return new ResponseEntity<>(reasonService.deleteReason(reason, now), HttpStatus.OK);
    }

    @GetMapping("/v1/reasons")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'MEMBER')")
    public ResponseEntity<?> getValidReason(){
        return new ResponseEntity<>(reasonService.getValidReasons(), HttpStatus.OK);
    }
}
