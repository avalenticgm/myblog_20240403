package it.cgmconsulting.myblog.configuration;

import it.cgmconsulting.myblog.service.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SendNewsletter {

    private final ConsentService consentService;

    //@Scheduled(fixedRate = 5000) // intervalli fissi espressi in millisecondi
    //@Scheduled(cron = "* * * * * *")
    @Scheduled(cron = "@weekly")
    public void sendNewsletterSched(){
        log.info("o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o");
        // recuperi i dati del consenso degli utenti per quelli che hanno frequency != NEVER
        // ed invii una mail con i post pubblicati
        // in base alla frequency e all'ultimo lastSent
    }

    /*
    @monthly
    once a month (0 0 0 1 * *)
    @weekly
    once a week (0 0 0 * * 0)
     */
}
