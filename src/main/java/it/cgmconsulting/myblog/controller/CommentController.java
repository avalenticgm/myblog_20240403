package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v1/comments")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest request,@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(commentService.createComment(request, userDetails), HttpStatus.CREATED);
    }
}
