package it.cgmconsulting.myblog.configuration;

import it.cgmconsulting.myblog.entity.Consent;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.enumeration.Frequency;
import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.service.ConsentService;
import it.cgmconsulting.myblog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SendNewsletter {

    private final ConsentService consentService;
    private final PostService postService;
    private final MailService mailService;

    //@Scheduled(fixedRate = 50000000) // intervalli fissi espressi in millisecondi
    //@Scheduled(cron = "* * * * * *")
    @Scheduled(cron = "@weekly")
    public void sendNewsletterSched(){
        log.info("o-o-o-o-o-o-o-o-o-o");
        //recuperi i dati del consenso degli utenti per quelli che hanno una frequency != never
        // ed invii una mail con i post pubblicati
        //in base alla frequency e all'ultimo lastSent
        List<Consent> newsletterMembers = consentService.getNewsletterMembers();
        List<Consent> weeklyFreqMembers = new ArrayList<>();
        List<Consent> monthlyFreqMembers = new ArrayList<>();
        for(Consent c : newsletterMembers){
            if(c.getFrequency().equals(Frequency.WEEKLY)){
                weeklyFreqMembers.add(c);
            } else {
                monthlyFreqMembers.add(c);
            }
        }
        List<Post> weeklyPosts = postService.getLastWeekPosts();
        List<Post> monthlyPosts = postService.getLastMonthPosts();
        StringBuilder weeklyMailContent = new StringBuilder();
        StringBuilder monthlyMailContent = new StringBuilder();
        //per utilizzare il formato html aggiungere il parametro true alla chiamata mimeMessageHelper.setText() nel metodo sendMail in MailService
        // -> mimeMessageHelper.setText(mail.getMailContent(), true);
        for(Post p : weeklyPosts){
            weeklyMailContent.append("<br> <a href='http://localhost:8090/api/v0/posts/").append(p.getId()).append("'>").append(p.getTitle()).append("</a>");
        }
        for(Post p : monthlyPosts){
            monthlyMailContent.append("<br> <a href='http://localhost:8090/api/v0/posts/").append(p.getId()).append("'>").append(p.getTitle()).append("</a>");
        }

        if(!weeklyPosts.isEmpty()){
            for(Consent c : weeklyFreqMembers){
                if (validWeeklyConsent(c)){
                    //metodo per invio email settimanale
                    mailService.sendMail(mailService.createMail(c.getConsentId().getUserId(), "I post dell'ultima settimana",
                            weeklyContentMail(c, weeklyMailContent)));
                    c.setLastSent(LocalDate.now());
                    consentService.save(c);
                }
            }
        }
        if(!monthlyPosts.isEmpty()){
            for(Consent c : monthlyFreqMembers){
                if(validMonthlyConsent(c)){
                    //metodo per invio email mensile
                    mailService.sendMail(mailService.createMail(c.getConsentId().getUserId(), "I post dell'ultimo mese",
                            monthlyContentMail(c, monthlyMailContent)));
                    c.setLastSent(LocalDate.now());
                    consentService.save(c);
                }
            }
        }
    }

    private boolean validWeeklyConsent(Consent c){
        return (c.getLastSent() == null ||
                LocalDate.now().minusWeeks(1).isAfter(c.getLastSent().minusDays(1))) &&
                c.getConsentId().getUserId().isEnabled();
    }

    private boolean validMonthlyConsent(Consent c){
        return (c.getLastSent() == null ||
                LocalDate.now().minusMonths(1).isAfter(c.getLastSent().minusDays(1))) &&
                c.getConsentId().getUserId().isEnabled();
    }

    private String weeklyContentMail(Consent c, StringBuilder s){
        return "Ciao " + c.getConsentId().getUserId().getUsername() + ", ecco i nuovi post di questa settimana: <br>" + s;
    }

    private String monthlyContentMail(Consent c, StringBuilder s){
        return "Ciao " + c.getConsentId().getUserId().getUsername() + ", ecco i nuovi post di questo mese: <br>" + s;
    }

}


    /*
    @monthly
    once a month (0 0 0 1 * *)
    @weekly
    once a week (0 0 0 * * 0)
     */

