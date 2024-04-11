package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private byte id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, unique = true)
    private AuthorityName authorityName;

    private boolean authorityDefault = false;
/*
    // PER RENDERE LA RELAZIONE BIDIREZIONALE CON USER
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();
*/
    public Authority(AuthorityName authorityName) {
        this.authorityName = authorityName;
    }

}
