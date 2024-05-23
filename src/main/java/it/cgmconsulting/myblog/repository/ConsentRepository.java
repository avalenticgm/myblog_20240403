package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Consent;
import it.cgmconsulting.myblog.entity.ConsentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsentRepository extends JpaRepository<Consent, ConsentId> {

    List<Consent> findBySendNewsletterTrue();

    @Query(value="SELECT * FROM consent c " +
            "WHERE c.send_newsletter = true " +
            "AND (" +
            "DATE_ADD(c.last_sent, INTERVAL 1 WEEK) = CURRENT_DATE " +
            "OR " +
            "DATE_ADD(c.last_sent, INTERVAL 1 MONTH) = CURRENT_DATE " +
            "OR c.last_sent IS NULL)", nativeQuery = true
    )
    List<Consent> getNewsletterConsent();

}
