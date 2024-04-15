package it.cgmconsulting.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "_user") // antepongo l'underscore per non avere problemi con termini che potrebbero essere interpretati come parole chiave dal db
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
public class User extends CreationUpdate implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true) // length default = 255
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password; // encrypted -> $2a$10$uRjzxWBfvrC5UPDwjpQoV.JsZLl6ClFHZuk9fAYW39T.n1PE021Km

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    private String bio;

    private boolean enabled =  false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authority",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany
    @JoinTable(name="preferred_post",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="post_id"))
    private Set<Post> preferredPosts = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /* Inherited methods from org.springframework.security.core.userdetails.UserDetails */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(authority ->
                new SimpleGrantedAuthority(authority.getAuthorityName().name())
        ).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
