package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonId;
import it.cgmconsulting.myblog.payload.response.ReasonResponse;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public String addReason(String reason, LocalDate startDate, int severity){
        // verificare che non esiste già un'altra reason con quel nome in corso di validità
        if(reasonRepository.existsByReasonIdReasonAndEndDateIsNull(reason))
            return null;
        Reason r = new Reason(
                new ReasonId(reason.toUpperCase(), startDate),
                severity
        );
        reasonRepository.save(r);
        return "Reason "+reason+" successfully created";
    }

    @Transactional
    public String deleteReason(String reason, LocalDate now){
        // verificare che ci sia una reason da invalidare,
        // ovvero che abbia endDate a null oppure che abbia un endDate futura
        List<Reason> validReasons = reasonRepository.getValidReason(reason, now);
        if(validReasons.isEmpty())
            return "No reason to invalidate";
        else if (validReasons.size() == 1) {
            validReasons.getFirst().setEndDate(now);
        }
        else {
            for(Reason r : validReasons){
                if(r.getEndDate() != null)
                    r.setEndDate(now);
                else
                   reasonRepository.delete(r);
            }
        }
        return "Reason "+reason+" invalidated";

    }

    public List<Reason> getValidReasons() {
        List<Reason> list = reasonRepository.getValidReasons(LocalDate.now());
        List<Reason> newList = new ArrayList<>();
        for(Reason r : list){
            // filtrare la lista in modo da non avere reason ripetute ma solo quelle effettivamente valide
        }
        return newList;
    }

}

/*
INSULTI RAZZIALI        2023-01-01  null
LINGUAGGIO NON CONSONO  2024-01-01  2024-12-31
LINGUAGGIO NON CONSONO  2025-01-01  null
LINK                    2023-01-01  null
TROLLING                2024-01-01  null
 */
/*
INSULTI RAZZIALI        2023-01-01  null
LINGUAGGIO NON CONSONO  2024-01-01  2024-12-31
LINK                    2023-01-01  null
TROLLING                2024-01-01  null
 */