package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.mail.Mail;
import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.payload.request.ChangeMeRequest;
import it.cgmconsulting.myblog.payload.request.ChangePasswordRequest;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.payload.response.GetMeResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


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
    private final AvatarRepository avatarRepository;



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

    @Transactional
    public String confirm(String confirmCode){
        // verificare che il token (confimCode) non sia scaduto
        Optional<Registration> reg = registrationService.findByConfirmCode(confirmCode);

        // Confirm code presente ma scaduto
        if(reg.isPresent() && LocalDateTime.now().isAfter(reg.get().getEndDate())) {
            Registration oldRegistration = reg.get();
            Registration newRegistration = Registration.builder()
                    .confirmCode(UUID.randomUUID().toString())
                    .endDate(LocalDateTime.now().plusMinutes(validity))
                    .user(oldRegistration.getUser())
                    .build();
            registrationService.save(newRegistration);
            registrationService.delete(oldRegistration);

            Mail mail = mailService.createMail(newRegistration.getUser(),
                    "Myblog - Registration confirm",
                    "Hi " + newRegistration.getUser().getUsername() + ",\n please click here to confirm your email \n http://localhost:8090/v0/auth?confirmCode=" + newRegistration.getConfirmCode());
            mailService.sendMail(mail);
            return "A new confirmation code has been sent to you. Please check your mail";
        }
        // confirm code NON presente
        else if(reg.isEmpty())
            return "You already confirmed your registration";
        // Confirm code presente e valido
        else {
            reg.get().getUser().setEnabled(true);
            Authority authority = authorityRepository.findByAuthorityName(AuthorityName.MEMBER);
            reg.get().getUser().setAuthorities(Collections.singleton(authority));
            registrationService.delete(reg.get());
            return "Now you are enabled. Please log in";
        }

    }

    @Transactional
    public AuthenticationResponse changeRole(int id, Set<String> auths, UserDetails userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if(userDetails.getUsername().equals(user.getUsername()))
            return null;
        Set<AuthorityName> authorityNames = EnumSet.noneOf(AuthorityName.class);
        for(String s : auths){
            authorityNames.add(AuthorityName.valueOf(s));
        }
        Set<Authority> authorities = authorityRepository.findByAuthorityNameIn(authorityNames);
        user.setAuthorities(authorities);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .build();

        return authenticationResponse;

    }

    public String changePassword(ChangePasswordRequest request, UserDetails userDetails){

        User user = (User) userDetails;

        if(!request.newPwd().equals(request.newPwdConfirm())){
            return "New Password and New Password Confirm are different";
        }

        if(!passwordEncoder.matches(request.oldPwd(), user.getPassword())){
           return "Wrong old password";
        }

        user.setPassword(passwordEncoder.encode(request.newPwd()));
        userRepository.save(user);
        return "New password has been set. Please re-login";

    }

    @Transactional
    public String resetPassword(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User", "username", username));
        String pwd = RandomStringUtils.randomAlphanumeric(10);

        Mail mail = mailService.createMail(user,
                "Myblog - Reset password request",
                "Hi " + user.getUsername() + ",\n use this password "+pwd+" to login");
        mailService.sendMail(mail);

        user.setPassword(passwordEncoder.encode(pwd));
        return "You password has been reset. Please check your email";
    }

    public GetMeResponse getMe(UserDetails userDetails){

        User user = (User) userDetails;
        Avatar avatar = avatarRepository.findById(new AvatarId(user)).orElse(null);
        AvatarResponse avatarResponse = null;
        if(avatar != null)
            avatarResponse = new AvatarResponse(avatar.getAvatarId().getUserId().getId(), avatar.getFilename(), avatar.getFiletype(), avatar.getData());

        GetMeResponse me = GetMeResponse.builder()
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .username(user.getUsername())
                .email((user.getEmail()))
                .bio(user.getBio())
                .avatar(avatarResponse)
                .build();

        return me;
    }

    public GetMeResponse changeMe(ChangeMeRequest request, UserDetails userDetails){
        User user = (User) userDetails;

        if(userRepository.existsByEmailAndIdNot(request.email(), user.getId()))
            return null;

        user.setBio(request.bio());
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setEmail(request.email());

        userRepository.save(user);

        GetMeResponse me = GetMeResponse.builder()
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .username(user.getUsername())
                .email((user.getEmail()))
                .bio(user.getBio())
                .build();

        return me;
    }

    public String deleteMe(UserDetails userDetails){
        User user = (User) userDetails;
        user.setEnabled(false);
        String s = UUID.randomUUID().toString();
        user.setEmail(s);
        user.setFirstname(s);
        user.setLastname(s);
        user.setBio(s);
        user.setUsername(s.substring(0,19));
        userRepository.save(user);
        return "Your account has been removed";
    }
}
