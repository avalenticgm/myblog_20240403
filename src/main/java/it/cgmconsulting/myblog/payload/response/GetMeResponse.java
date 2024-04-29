package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Avatar;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GetMeResponse {

    private int id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String bio;
    private AvatarResponse avatar;
}
