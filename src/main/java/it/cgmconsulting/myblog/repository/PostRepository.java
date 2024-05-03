package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // JPQL -> Java Persistence Query Language
    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostDetailResponse(" +
            "p.title, " +
            "p.content, " +
            "p.image, " +
            "p.publicationDate, " +
            "p.totComments, " +
            //"AVG(r.rate) AS average, " +
            "COALESCE(AVG(r.rate), 0d), " +
            "p.userId.username" +
            ") FROM Post p " +
            "LEFT JOIN Rating r ON r.ratingId.postId.id = p.id " +
            "WHERE p.id = :id " +
            "AND (p.publicationDate IS NOT NULL AND p.publicationDate <= :now)")
    Optional<PostDetailResponse> getPostById(int id, LocalDate now);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostResponse(" +
            "p.title, " +
            "p.overview, " +
            "p.image, " +
            "p.publicationDate, " +
            "p.totComments," +
            "(SELECT AVG(r.rate) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average" +
            ") FROM Post p " +
            "WHERE (p.publicationDate IS NOT NULL " +
            "AND p.publicationDate <= :now)")
    List<PostResponse> getVisiblePosts(LocalDate now);
}
