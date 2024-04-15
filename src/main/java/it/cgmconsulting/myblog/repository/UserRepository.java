package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT u.id FROM user_ u WHERE username='username' OR email='email'
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);
}
