package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Short> {

    List<Tag> findByVisibleTrueOrderByTagName(); // select * from tag where visible = true order by tag_name asc
    List<Tag> findByVisibleFalseOrderByTagName(); // select * from tag where visible = false order by tag_name asc
    List<Tag> findAllByOrderByTagName(); // select * from tag order by tag_name asc

    boolean existsByTagName(String tagName);
    boolean existsByTagNameAndIdNot(String newTagName, short id);

    Optional<Tag> findByTagName(String tagName);

    Optional<Tag> findByTagNameAndVisibleTrue(String tagName); // select * from tag where tag_name= 'xxxx' AND visible = true
}