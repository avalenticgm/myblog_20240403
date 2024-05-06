package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @Min(1)
    private int commentId;
    @NotBlank @Size(max=255, min=2)
    private String comment;
}
