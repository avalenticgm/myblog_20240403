package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AvatarResponse {

    private int id;
    private String filename;
    private String filetype;
    private byte[] data;
}
