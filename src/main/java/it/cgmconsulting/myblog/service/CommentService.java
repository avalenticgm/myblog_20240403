package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.request.CommentUpdateRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Value("${application.comment.time}")
    private int timeToUpdate;

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponse createComment(CommentRequest request, UserDetails userDetails){
        Post post = postService.findPostById(request.getPostId());
        Comment parent = null;
        if (request.getCommentId() != null)
            parent = findCommentById(request.getCommentId());

        Comment comment = new Comment(
                request.getComment(),
                (User) userDetails,
                post,
                parent);
        commentRepository.save(comment);

        post.setTotComments((short) (post.getTotComments() + 1));

        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUserId().getUsername(),
                comment.getCreatedAt(),
                comment.getParent() != null ? comment.getParent().getId() : null
                );
    }

    public Comment findCommentById(int id){
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Id", id));
    }

    @Transactional
    public String updateComment(CommentUpdateRequest request, UserDetails userDetails){
        Comment comment = findCommentById(request.getCommentId());
        // verifica autore commento
        if(!((User) userDetails).equals(comment.getUserId()))
            return "you can update only your comments";
        // verifica tempo passato dalla creazione
        if(comment.isCensored())
            return "you cannot update a censored comment";
        if( comment.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(timeToUpdate)) )
            return "you can update the comment only "+timeToUpdate+"sec after creation";
        // set del nuovo testo
        comment.setComment(request.getComment());
        return "your comment has been updated";

    }

    @Transactional
    public String deleteComment(int id, UserDetails userDetails) {
        Comment comment = findCommentById(id);
        // verifica autore commento
        if(!((User) userDetails).equals(comment.getUserId()))
            return "you can delete only your comments";
        if(comment.isCensored())
            return "you cannot delete a censored comment";
        if( comment.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(timeToUpdate)) )
            return "you can delete the comment only "+timeToUpdate+"sec after creation";
        comment.getPostId().setTotComments((short) (comment.getPostId().getTotComments() - 1));
        commentRepository.deleteById(id);
        return "your comment has been deleted";
    }

    public List<CommentResponse> getComments(int postId) {
        return commentRepository.getComments(postId, LocalDateTime.now().minusSeconds(timeToUpdate));
    }

}
