package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tags") // http://localhost:8090/tags/...
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/{id}") // api/vi/tags/5
    public ResponseEntity<?> getTag(@PathVariable @Min(1) @Max(32767) short id){
        return new ResponseEntity<>(tagService.getTag(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestParam @NotBlank(message="The tag name  must contain at least one non-whitespace character!") @Size(max=50, min=4) String tagName){
        Tag tag = tagService.createTag(tagName.toUpperCase());
        if(tag != null)
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        return new ResponseEntity<>("The tag with name "+tagName+" is already present", HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<?> updateTag(
            @RequestParam @NotBlank @Size(max=50, min=4) String tagName,
            @RequestParam @NotBlank @Size(max=50, min=4) String newTagName,
            @RequestParam boolean visible){
        Tag tag = tagService.updateTag(tagName.toUpperCase(), newTagName.toUpperCase(), visible);
        if(tag != null)
            return new ResponseEntity<>(tag, HttpStatus.OK);
        return new ResponseEntity<>("The tag with name "+tagName+" is already present", HttpStatus.CONFLICT);
    }
}
