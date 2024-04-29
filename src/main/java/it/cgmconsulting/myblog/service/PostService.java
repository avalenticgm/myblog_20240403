package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public String  createPost(PostRequest request, UserDetails userDetails){
        Post post = new Post(request.getTitle(), request.getContent(), (User) userDetails);
        postRepository.save(post);
        return "New post having title "+post.getTitle()+" has been created";
    }
}
