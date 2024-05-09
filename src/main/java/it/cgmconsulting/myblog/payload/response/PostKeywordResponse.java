package it.cgmconsulting.myblog.payload.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(callSuper = false)
public class PostKeywordResponse extends PostResponse{

    private String content;

    public PostKeywordResponse(int id, String title, String overview, String image, short totComments, double average, String content) {
        super(id, title, overview, image, totComments, average);
        this.content = content;
    }
}