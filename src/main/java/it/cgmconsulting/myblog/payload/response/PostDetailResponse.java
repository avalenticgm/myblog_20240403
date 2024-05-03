package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostDetailResponse {

    private String title;
    private String content;
    private String image;
    private LocalDate publicationDate;
    private short totComments;
    private double average; // i voti vanno da 1 a 5, quindi una media a 0 indica che il post non è mai stato votato
    private String author;
    private List<CommentResponse> comments = new ArrayList<>();

    public PostDetailResponse(String title, String content, String image, LocalDate publicationDate, short totComments, double average, String author) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.publicationDate = publicationDate;
        this.totComments = totComments;
        this.average = average;
        this.author = author;
    }
}