package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.TagRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;

    public String  createPost(PostRequest request, UserDetails userDetails){
        Post post = new Post(request.getTitle(), request.getContent(), (User) userDetails);
        postRepository.save(post);
        return "New post having title "+post.getTitle()+" has been created";
    }

    public String editPost(int id, PostRequest request, UserDetails userDetails){
        User user = (User) userDetails;
        Post post = findPostById(id);
        boolean isAdmin = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ADMIN"::equals);

        if( user.getId() == post.getUserId().getId() || isAdmin ){
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            post.setUpdatedAt(LocalDateTime.now());
            post.setPublicationDate(null);
            postRepository.save(post);

            return "Post edited successfully";
        }
        else return null;
    }

    public PostDetailResponse getPost(int id){
        PostDetailResponse postDetailResponse = postRepository.getPostById(id, LocalDate.now()).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
        Set<String> tagNames = tagService.getTagNamesByPost(id);
        if(!tagNames.isEmpty()){
            postDetailResponse.setTagNames(tagNames);
        }
        return postDetailResponse;
    }

    public List<PostResponse> getAllVisiblePosts() {
        return postRepository.getVisiblePosts(LocalDate.now());
    }

    @Transactional
    public String publishPost(int id, LocalDate publicationDate){
        Post post = findPostById(id);
        post.setPublicationDate(publicationDate);
        return "Publication post on "+publicationDate;
    }

    @Transactional
    public void addUpdateTagsToPost(UserDetails userDetails, int id, Set<String> tagNames) {
        User user = (User) userDetails;
        Post post = findPostById(id);
        boolean isAdmin = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ADMIN"::equals);

        if( user.equals(post.getUserId()) || isAdmin ){
            Set<Tag> tags = tagService.findAllByVisibleTrueAndTagNameIn(tagNames);
            post.setTags(tags);
        }
    }

    public Post findPostById(int id){
        return postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
    }


}
