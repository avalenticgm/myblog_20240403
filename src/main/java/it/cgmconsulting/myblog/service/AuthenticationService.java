package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.Registration;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.mail.Mail;
import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Value("${application.confirmCode.validity}")
    private long validity;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final RegistrationService registrationService;



    @Transactional
    public String signup(SignupRequest request) {
        if(userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            return null;
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        Authority authority = authorityRepository.findByAuthorityDefaultTrue();
        user.setAuthorities(Collections.singleton(authority));
        userRepository.save(user);

        Registration registration = Registration.builder()
                .confirmCode(UUID.randomUUID().toString())
                .endDate(LocalDateTime.now().plusMinutes(validity))
                .user(user)
                .build();
        registrationService.save(registration);

        Mail mail = mailService.createMail(user,
                "Myblog - Registration confirm",
                "Hi " + user.getUsername() + ",\n please click here to confirm your email \n http://localhost:8090/v0/auth?confirmCode=" + registration.getConfirmCode());
        mailService.sendMail(mail);
        return "User succesfully registered. Please check your email to confirm the registration.";
    }

    public AuthenticationResponse signin(SigninRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        boolean isGuest = isGuest(authorities(user.getAuthorities()));

        // caso in cui l'utente non abbia ancora confermato la propria email
        if(!user.isEnabled() && isGuest)
            throw new DisabledException("You didn't confirm your email still");

        // caso in cui l'utente sia stato bannato
        if(!user.isEnabled() && !isGuest)
            throw new DisabledException("You are banned");

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

    private boolean isGuest(String[] authorities){
        return Arrays.stream(authorities).anyMatch(s -> s.contains(authorityRepository.findByAuthorityDefaultTrue().getAuthorityName().name()));
    }

    private boolean confirm(String confirmCode){
        // verificare che il token non sia scaduto
        Registration reg = registrationService.
        // se è valido, abilitare lo user, cambiargli l'authority
        // altrimenti reinviare l'email con nuovo token
    }

}
