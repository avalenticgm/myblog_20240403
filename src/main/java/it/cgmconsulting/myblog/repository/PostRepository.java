package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostKeywordResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import org.springframework.beans.factory.annotation.Value;
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



    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.image, " +
            "p.totComments," +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average" +
            ") FROM Post p " +
            "INNER JOIN p.tags t ON (t.tagName = :tag AND t.visible = true) " +
            "WHERE (p.publicationDate IS NOT NULL " +
            "AND p.publicationDate <= :now)")
    Page<PostResponse> getVisiblePostsByTag(LocalDate now, Pageable pageable, String tag);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.image, " +
            "p.totComments," +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average" +
            ") FROM Post p " +
            "WHERE (p.publicationDate IS NOT NULL " +
            "AND p.publicationDate <= :now) " +
            "AND p.userId.username = :username")
    Page<PostResponse> getVisiblePostsByAuthor(LocalDate now, Pageable pageable, String username);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostKeywordResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.image, " +
            "p.totComments," +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.postId.id = p.id) AS average, " +
            "p.content" +
            ") FROM Post p " +
            "WHERE (p.publicationDate IS NOT NULL AND p.publicationDate <= :now) " +
            "AND (p.title LIKE :keyword OR p.content LIKE :keyword)")
    Page<PostKeywordResponse> getVisiblePostsByKeyword(LocalDate now, Pageable pageable, String keyword);
}
