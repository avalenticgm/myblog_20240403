package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final CommentService commentService;
    private final ReasonService reasonService;

    public String createReport(UserDetails userDetails, int commentId, String reason, LocalDate startDate){

        // istanziare un oggetto commento: verificare che il commento esista e che non sia già stato segnalato
        Comment c = commentService.getCommentToReport(commentId);

        // autore del commento e utente segnalante non devono coincidere
        User reporter = (User) userDetails;
        if(c.getUserId().equals(reporter))
            return "You cannot report yourself";

        // istanziare un oggetto Reason: recuperarlo dal db
        Reason r = reasonService.findReasonById(new ReasonId(reason, startDate));

        // istanziare un oggetto Reporting e salvarlo
        Reporting rep = new Reporting(new ReportingId(c), r, reporter);

        reportingRepository.save(rep);

        return "The comment '"+c.getComment()+"' has been reported";
    }
}
