package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostDetailResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.service.ImageService;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @Value("${application.image.post.size}")
    private long size;
    @Value("${application.image.post.width}")
    private int width;
    @Value("${application.image.post.height}")
    private int height;
    @Value("${application.image.post.extensions}")
    String[] extensions;

    @PostMapping("/v1/posts")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid PostRequest request){
        return new ResponseEntity<>(postService.createPost(request, userDetails), HttpStatus.CREATED);
    }

    @PutMapping("/v1/posts")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam @Min(1) int id,
                                      @RequestBody @Valid PostRequest request) {
        String s = postService.editPost(id, request, userDetails);
        if(s != null)
            return new ResponseEntity<>(s, HttpStatus.OK);
        else return new ResponseEntity<>("You cannot edit this post", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/v0/posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id){
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }
    @GetMapping("/v0/posts")
    public ResponseEntity<?> getAllVisiblePosts(
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // indica se l'ordinamento è ASC o DESC
    ){
        List<PostResponse> list = postService.getAllVisiblePosts(pageNumber, pageSize, sortBy, direction);
        if(list.isEmpty())
            return new ResponseEntity<>("No posts found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/v1/posts/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> publishPost(@PathVariable int id, @RequestParam @FutureOrPresent LocalDate publicationDate){
        return new ResponseEntity<>(postService.publishPost(id, publicationDate), HttpStatus.OK);
    }

    @PatchMapping("/v1/posts/tags/{id}")
    @PreAuthorize("hasAnyAuthority('WRITER', 'ADMIN')")
    public ResponseEntity<?> addUpdateTagsToPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int id,
            @RequestParam Set<String> tagNames){
        postService.addUpdateTagsToPost(userDetails, id, tagNames);
        return new ResponseEntity<>("Tags added to post", HttpStatus.OK);
    }

    @PatchMapping(value="/v1/posts/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('WRITER')")
    public ResponseEntity<?> addimageToPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam MultipartFile file,
            @PathVariable int id) throws IOException {
        String msg = imageService.addPostImage(userDetails, size, width, height, extensions, file, id);
        if(msg != null)
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong uploading the image", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // ricerche per tag, autore e parola chiave

    @GetMapping("/v0/posts/tags")
    public ResponseEntity<?> getAllVisiblePostsByTag(
            @RequestParam @Size(max=50) @NotBlank String tag,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // indica se l'ordinamento è ASC o DESC
    ){
        List<PostResponse> list = postService.getAllVisiblePostsByTag(pageNumber, pageSize, sortBy, direction, tag);
        if(list.isEmpty())
            return new ResponseEntity<>("No posts found with tag "+tag, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/v0/posts/author")
    public ResponseEntity<?> getAllVisiblePostsByAuthor(
            @RequestParam @Size(max=20) @NotBlank String username,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // indica se l'ordinamento è ASC o DESC
    ){
        List<PostResponse> list = postService.getAllVisiblePostsByAuthor(pageNumber, pageSize, sortBy, direction, username);
        if(list.isEmpty())
            return new ResponseEntity<>("No posts found written by "+username, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/v0/posts/keyword")
    public ResponseEntity<?> getAllVisiblePostsByKeyword(
            @RequestParam @Size(min=3) @NotBlank String keyword,
            @RequestParam(defaultValue = "false") boolean isCaseSensitive,
            @RequestParam(defaultValue = "false") boolean isExactMatch,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire
            @RequestParam(defaultValue = "10") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // indica se l'ordinamento è ASC o DESC
    ){
        List<PostResponse> list = postService.getAllVisiblePostsByKeyword(pageNumber, pageSize, sortBy, direction, keyword, isCaseSensitive, isExactMatch);
        if(list.isEmpty())
            return new ResponseEntity<>("No posts found containing "+keyword, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
        /*
        * isExactMatch = true,  keyword 'mare' : Il mare è blu; Vado al mare. ;
        * isExactMatch = false, keyword 'mare' : Il maremoto è terribile; amare i fiori;
        * */
    }

    // aggiunta o la rimozione di un bookmark
    @PostMapping("/v1/posts/bookmark")
    @PreAuthorize("hasAuthority('MEMBER')") // SpEL -> Spring Expression Language
    public ResponseEntity<?> addRemoveBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @Min(1) int postId){
        return new ResponseEntity<>(postService.addRemoveBookmark(userDetails, postId), HttpStatus.OK);
    }

    @GetMapping("/v1/posts/bookmark")
    @PreAuthorize("hasAuthority('MEMBER')") // SpEL -> Spring Expression Language
    public ResponseEntity<?> getBookmarks(
            @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(postService.getBookmarks(userDetails), HttpStatus.OK);
    }

}
