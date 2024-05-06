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

    // PARENT
    // Post : spaghetti alla carbonara
    //      c1: buoni!; 5; 1; null
    //          c2: non è vero fanno schifo!; 6; 1; c1
    //              c3: tu fai schifo! 5; 1; c2
    //              c5: ha ragione pippo, tu fai schifo; 12, 1; c2
    //      c4: con il guanciale è ottimo; 8; 1; null
}
