package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Short> {


    // SQL nativo
    @Query(value="SELECT t.tag_name FROM post_tags pt " +
            "INNER JOIN tag t ON pt.tag_id = t.id " +
            "AND pt.post_id = :postId", nativeQuery = true)
    Set<String> getTagNamesByPost(int postId);

    List<Tag> findByVisibleTrueOrderByTagName(); // select * from tag where visible = true order by tag_name asc
    List<Tag> findByVisibleFalseOrderByTagName(); // select * from tag where visible = false order by tag_name asc
    List<Tag> findAllByOrderByTagName(); // select * from tag order by tag_name asc

    Set<Tag> findAllByVisibleTrueAndTagNameIn(Set<String> tagNames); // select * from tag where visible = 1 AND tag_name IN('xxx','yyy', etc..)

    boolean existsByTagName(String tagName);
    boolean existsByTagNameAndIdNot(String newTagName, short id);

    Optional<Tag> findByTagName(String tagName);

    Optional<Tag> findByTagNameAndVisibleTrue(String tagName); // select * from tag where tag_name= 'xxxx' AND visible = true
}
