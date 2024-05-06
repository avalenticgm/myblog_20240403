package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotBlank @Size(max=255, min=2)
    private String comment;
    @Min(1)
    private int postId;
    @Min(1)
    private Integer commentId;
}
