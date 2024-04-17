package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v0/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        String msg = authenticationService.signup(request);
        if(msg == null)
            return new ResponseEntity<>("Username or email already in use", HttpStatus.CONFLICT);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PostMapping("/v0/auth/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninRequest request){
        return new ResponseEntity<>(authenticationService.signin(request), HttpStatus.OK);
    }

    @PatchMapping("/v0/auth")
    public ResponseEntity<?> confirm(@RequestParam @NotBlank @Size(max=36) String confirmCode){

    }


}
