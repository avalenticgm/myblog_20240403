package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Modifying
    @Transactional
    @Query(value="DELETE FROM rating " +
            "WHERE post_id = :postId " +
            "AND user_id = :userId", nativeQuery = true)
    void deleteRating(int postId, int userId);


    @Query(value="SELECT r.rate " +
            "FROM Rating r " +
            "WHERE r.ratingId.postId.id = :postId " +
            "AND r.ratingId.userId.id = :userId")
    Byte getMyRate(int postId, int userId); // uso il wrapper perchè se non c'è un record viene restituito null

}

