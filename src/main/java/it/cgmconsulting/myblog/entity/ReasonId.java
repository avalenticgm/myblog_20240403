package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ReasonId implements Serializable {

    @Column(length = 30, nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate startDate;

}
