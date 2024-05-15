package it.cgmconsulting.myblog.repository;


import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {

}
