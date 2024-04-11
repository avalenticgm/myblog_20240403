package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Rating extends CreationUpdate {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    @Check(constraints = "rate > 0 AND rate < 6")
    private byte rate; // espresso con le stelline da 1 a 5
    
}
