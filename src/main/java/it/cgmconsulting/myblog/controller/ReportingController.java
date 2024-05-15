package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.service.ReportingService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Validated
public class ReportingController {

    private final ReportingService reportingService;

    @PostMapping("/v1/reportings")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<String> createReport(
            @AuthenticationPrincipal UserDetails userDetails, // colui che segnala il commento
            @RequestParam @Min(1) int commentId,
            @RequestParam @NotBlank @Size(min=3, max=30) String reason, // reason e startDate formano la PK per l'entità Reason
            @RequestParam @NotNull LocalDate startDate
    ){
        return new ResponseEntity<String>(reportingService.createReport(userDetails, commentId, reason, startDate), HttpStatus.CREATED);
    }

    @PutMapping("/v1/reportings")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> updateReport(
            @RequestParam @NotBlank @Size(min=3, max=30) String reason, // reason e startDate formano la PK per l'entità Reason
            @RequestParam @NotNull LocalDate startDate,
            @RequestParam ReportingStatus status,
            @RequestParam @Min(1) int commentId
    ){
        return new ResponseEntity<String>(reportingService.updateReport(reason, startDate, status, commentId), HttpStatus.OK);
    }
}
