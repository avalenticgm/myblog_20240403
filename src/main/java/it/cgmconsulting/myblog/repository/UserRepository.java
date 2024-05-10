package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT u.id FROM user_ u WHERE username='username' OR email='email'
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmailAndIdNot(String email, int id);

    @Query(value="SELECT u FROM User u " +
            "LEFT JOIN FETCH u.preferredPosts pp " + // FETCH forza il fetchType a EAGER
            "WHERE u.id = :userId")
    User getUserWithPreferredPost(int userId);
}
