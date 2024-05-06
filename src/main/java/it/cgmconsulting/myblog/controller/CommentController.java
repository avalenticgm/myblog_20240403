package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.request.CommentUpdateRequest;
import it.cgmconsulting.myblog.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v1/comments")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest request,@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(commentService.createComment(request, userDetails), HttpStatus.CREATED);
    }

    @PatchMapping("/v1/comments")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> updateComment(@RequestBody @Valid CommentUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(commentService.updateComment(request, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/v1/comments/{id}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> deleteComment(@PathVariable @Min(1) int id, @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(commentService.deleteComment(id, userDetails), HttpStatus.OK);
    }

    // Get List<Comment> /v0/comments
    // verificare se censurati
    // creati almeno 1 minuto prima della get
    // ordinati per updatedAt DESC
}
