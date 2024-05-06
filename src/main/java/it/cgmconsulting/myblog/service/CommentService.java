package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentResponse createComment(CommentRequest request, UserDetails userDetails){
        Post post = postService.findPostById(request.getPostId());
        Comment parent = null;
        if(request.getCommentId() != null)
            parent = findCommentById(request.getCommentId());

        Comment comment = new Comment(
                request.getComment(),
                (User) userDetails,
                post,
                parent);
        commentRepository.save(comment);

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
}
