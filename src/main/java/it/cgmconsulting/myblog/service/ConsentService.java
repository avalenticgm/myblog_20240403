package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Consent;
import it.cgmconsulting.myblog.entity.ConsentId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.Frequency;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;

    public void save(Consent consent){
        consentRepository.save(consent);
    }

    public String updateConsent(UserDetails userDetails, Frequency frequency){

        User user = (User) userDetails;
        Consent consent = getConsentById(new ConsentId(user));

        if(frequency.equals(Frequency.NEVER)) {
            consent.setSendNewsletter(false);
            consent.setFrequency(Frequency.NEVER);
        } else if (frequency.equals(Frequency.MONTHLY)) {
            consent.setSendNewsletter(true);
            consent.setFrequency(Frequency.MONTHLY);
        } else {
            consent.setSendNewsletter(true);
            consent.setFrequency(Frequency.WEEKLY);
        }
        save(consent);

        return "Consent successfully updated";
    }

    public Consent getConsentById(ConsentId consentId) {
        return consentRepository.findById(consentId).orElseThrow(
                () -> new ResourceNotFoundException("Consent", "id", consentId));
    }

    public List<Consent> getNewsletterConsent(){
        return consentRepository.getNewsletterConsent();
    }

    public List<Consent> getNewsletterMembers(){
        return consentRepository.findBySendNewsletterTrue();
    }
}
