package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AvatarController {

    private final ImageService imageService;

    @Value("${application.image.avatar.size}")
    private long size;
    @Value("${application.image.avatar.width}")
    private int width;
    @Value("${application.image.avatar.height}")
    private int height;
    @Value("${application.image.avatar.extensions}")
    String[] extensions;

    @PostMapping(value = "/v1/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam MultipartFile file) throws IOException {
        AvatarResponse avatar = imageService.addAvatar(userDetails, size, width, height, extensions, file);
        if(avatar != null)
            return new ResponseEntity<>(avatar, HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong during the file upload", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/v1/avatar")
    public ResponseEntity<?> deleteAvatar(@AuthenticationPrincipal UserDetails userDetails){
        imageService.removeAvatar(userDetails);
        return new ResponseEntity<>("Your avatar has been removed", HttpStatus.OK);
    }

}
