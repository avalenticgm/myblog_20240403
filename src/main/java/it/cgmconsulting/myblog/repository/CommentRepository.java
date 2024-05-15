package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
            "c.id, " +
            "CASE WHEN c.censored = false THEN c.comment ELSE '**********' END, " +
            "c.userId.username, " +
            "c.updatedAt, " +
            "c.parent.id " +
            ") FROM Comment c " +
            "WHERE c.postId.id = :postId " +
            "AND c.updatedAt <= :now " +
            "ORDER BY c.updatedAt DESC ")
    List<CommentResponse> getComments(int postId, LocalDateTime now);

    @Query(value="SELECT c FROM Comment c " +
            "LEFT JOIN Reporting r ON c = r.reportingId.commentId " +
            "WHERE c.id = :commentId " +
            "AND r.createdAt IS NULL ")
    Optional<Comment> getCommentToReport(int commentId);

}
