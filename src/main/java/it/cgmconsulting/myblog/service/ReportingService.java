package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public String updateReport(String reason, LocalDate startDate, ReportingStatus status, int commentId) {
        // trovare il report e verificare che non sia già stato chiuso
        Comment comment = commentService.findCommentById(commentId);
        Reporting rep = findById(new ReportingId(comment));

        if(rep.getStatus().name().startsWith("CLOSED"))
            return "The report i already in status 'CLOSED'";

        else if(rep.getStatus().equals(ReportingStatus.IN_PROGRESS) && status.equals(ReportingStatus.NEW))
            return "Changing status not allowed";

        else {
            if(status.equals(ReportingStatus.CLOSED_WITH_BAN)){
                comment.setCensored(true);
                comment.getUserId().setEnabled(false);
            }
            rep.setStatus(status);
        }
        return null;
    }

    public Reporting findById(ReportingId reportingId) {
        return reportingRepository.findById(reportingId).orElseThrow(
                () -> new ResourceNotFoundException("Reporting", "comment",reportingId.getCommentId().getId()));
    }

}


