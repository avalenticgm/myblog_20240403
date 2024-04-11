package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Reporting extends CreationUpdate {

    @EmbeddedId
    private ReportingId reportingId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="reason_name"),
            @JoinColumn(name="reason_start_date")
    })
    private Reason reason;

    @Enumerated(EnumType.STRING)
    private ReportingStatus status = ReportingStatus.NEW;

    public Reporting(ReportingId reportingId, Reason reason) {
        this.reportingId = reportingId;
        this.reason = reason;
    }
}
