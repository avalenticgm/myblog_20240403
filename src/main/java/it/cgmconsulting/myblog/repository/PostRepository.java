package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // JPQL -> Java Persistence Query Language
    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostDetailResponse(" +
            "p.id, " +
            "p.title, " +
            "p.content, " +
            "p.image, " +
            "p.publicationDate, " +
            "p.totComments, " +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average, " +
            "p.userId.username" +
            ") FROM Post p " +
            "WHERE p.id = :id " +
            "AND (p.publicationDate IS NOT NULL AND p.publicationDate <= :now)")
    Optional<PostDetailResponse> getPostById(int id, LocalDate now);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.image, " +
            "p.totComments," +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average" +
            ") FROM Post p " +
            "WHERE (p.publicationDate IS NOT NULL " +
            "AND p.publicationDate <= :now)")
    Page<PostResponse> getVisiblePosts(LocalDate now, Pageable pageable);
}
