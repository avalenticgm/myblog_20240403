package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthenticationResponse {

    private int id;
    private String username;
    private String email;
    private String[] authorities;
    private String token;


}
