package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.ChangeMeRequest;
import it.cgmconsulting.myblog.payload.request.ChangePasswordRequest;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.payload.response.GetMeResponse;
import it.cgmconsulting.myblog.service.AuthenticationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
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
        return new ResponseEntity<>(authenticationService.confirm(confirmCode), HttpStatus.OK);
    }

    @PatchMapping("/v1/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeRole(
            @PathVariable @Min(1) int id,
            @RequestParam @NotEmpty Set<String> auths,
            @AuthenticationPrincipal UserDetails userDetails){

        AuthenticationResponse a = authenticationService.changeRole(id, auths, userDetails);
        if(a != null)
            return new ResponseEntity<>(a, HttpStatus.OK);
        else
            return new ResponseEntity<>("You cannot change your own role", HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/v1/user")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(authenticationService.changePassword(request, userDetails), HttpStatus.OK);
    }

    @PatchMapping("/v0/user/reset")
    public ResponseEntity<?> resetPassword(@RequestParam @NotBlank String username){
        return new ResponseEntity<>(authenticationService.resetPassword(username), HttpStatus.OK);
    }

    @GetMapping("/v1/user")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(authenticationService.getMe(userDetails), HttpStatus.OK);
    }

    @PutMapping("/v1/user")
    public ResponseEntity<?> changeMe(@RequestBody @Valid ChangeMeRequest request, @AuthenticationPrincipal UserDetails userDetails){
        GetMeResponse me = authenticationService.changeMe(request, userDetails);
        if(me == null)
            return new ResponseEntity<>("Email alredy in use", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(me, HttpStatus.OK);
    }

    @PutMapping("/v1/user/remove")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(authenticationService.deleteMe(userDetails), HttpStatus.OK);

    }

}
