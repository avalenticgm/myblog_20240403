package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostResponse {

    private int id;
    private String title;
    private String overview;
    private String image;
    private short totComments;
    private double average;

}