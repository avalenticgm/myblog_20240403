package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tags") // http://localhost:8090/tags/...
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    // Y = only visible
    // N = only not visible
    // A = All (visible and not visible)
    public ResponseEntity<?> getAllTags(@RequestParam(defaultValue = "Y") Character visible){
        List<Tag> tags = tagService.getAllTags(visible);
        if(tags.isEmpty())
            return new ResponseEntity<String>("No tags found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<List<Tag>>(tags, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestParam @NotBlank @Size(max=50, min=4) String tagName){
        return new ResponseEntity<>(tagService.createTag(tagName.toUpperCase()), HttpStatus.CREATED);
    }
}
