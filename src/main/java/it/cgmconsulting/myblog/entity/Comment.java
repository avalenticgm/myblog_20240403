package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Comment extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post postId;

    @ManyToOne
    @JoinColumn(name="parent")
    private Comment parent;

    private boolean censored = false;

    public Comment(String comment, User userId, Post postId, Comment parent) {
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;
        this.parent = parent;
    }
}
