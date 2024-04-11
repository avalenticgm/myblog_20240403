package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.AvatarId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, AvatarId> {
}
