package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank @Size(max=255, min=1)
    private String title;
    @NotBlank @Size(max=65535, min=10)
    private String content;
}
