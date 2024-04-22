package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Byte> {

    Authority findByAuthorityDefaultTrue();
    Authority findByAuthorityName(AuthorityName authorityName);

    Set<Authority> findByAuthorityNameIn(Set<AuthorityName> authorityNames);
}
