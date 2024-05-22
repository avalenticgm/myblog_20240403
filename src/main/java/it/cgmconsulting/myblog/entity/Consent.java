package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumeration.Frequency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Consent extends CreationUpdate {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ConsentId consentId;

    private boolean acceptRules;
    private boolean sendNewsletter;
    @Enumerated(EnumType.STRING)
    private Frequency frequency = Frequency.NEVER;

    private LocalDate lastSent;

}
