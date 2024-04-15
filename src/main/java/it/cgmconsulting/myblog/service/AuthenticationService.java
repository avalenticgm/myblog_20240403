package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public String signup(SignupRequest request){
        if(userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            return null;
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        Authority authority = authorityRepository.findByAuthorityDefaultTrue();
        user.setAuthorities(Collections.singleton(authority));

        userRepository.save(user);
        return "User succesfully registered. Please check your email to confirm the registration.";
    }

    public AuthenticationResponse signin(SigninRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        String jwt = jwtService.generateToken(user, user.getId());

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .token(jwt)
                .build();

        return authenticationResponse;
    }

    private String[] authorities(Collection<? extends GrantedAuthority> auths){
        return auths.stream().map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }


}
