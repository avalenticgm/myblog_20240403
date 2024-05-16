package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReportingDetailResponse {

    private int commentId;
    private ReportingStatus status;
    private String reason;
    private String comment;
    private String reporter; // username dello user che ha aperto la segnalazione
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
