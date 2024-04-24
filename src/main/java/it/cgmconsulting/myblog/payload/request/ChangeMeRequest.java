package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeMeRequest(

        @Size(max=50, min=2)
        String firstname,
        @Size(max=50, min=2)
        String lastname,
        @Size(max=255)
        String bio,
        @NotBlank @Email
        String email
) {
}
