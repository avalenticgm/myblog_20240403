package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReportingResponse {

    private int commentId;
    private ReportingStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
