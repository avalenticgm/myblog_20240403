package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // Questo metodo scrive il voto se non esiste e lo aggiorna se già esiste
    @PostMapping("/v1/rating")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> addVote(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @Min(1) int postId,
            @RequestParam @Range(min=1, max=5) byte rate
    ){
        return new ResponseEntity<>(ratingService.addRate(userDetails, postId, rate), HttpStatus.OK);
    }

    //rimozione voto
    @DeleteMapping("/v1/rating")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> deleteVote(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @Min(1) int postId
    ){
        ratingService.deleteRate(userDetails, postId);
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

    //get del proprio voto
    @GetMapping("/v1/rating")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> getMyRate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @Min(1) int postId
    ){
        return new ResponseEntity<>(ratingService.getMyRate(userDetails, postId), HttpStatus.OK);
    }
}
