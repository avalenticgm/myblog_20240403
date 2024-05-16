package it.cgmconsulting.myblog.repository;


import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.entity.enumeration.ReportingStatus;
import it.cgmconsulting.myblog.payload.response.ReportingResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.ReportingResponse(" +
            "r.reportingId.commentId.id, " +
            "r.status, " +
            "r.reason.reasonId.reason, " +
            "r.createdAt, " +
            "r.updatedAt" +
            ") FROM Reporting r " +
            "WHERE r.status = :status")
    Page<ReportingResponse> getReportings(ReportingStatus status, Pageable pageable);

}
