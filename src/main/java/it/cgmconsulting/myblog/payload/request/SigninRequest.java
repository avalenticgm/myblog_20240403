package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SigninRequest {

    @NotBlank @Size(max=20, min=4)
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message="La password può contenere solo caratteri maiuscoli e/o minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri ")
    private String password;
}
