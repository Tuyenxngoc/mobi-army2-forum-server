package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByOrderByLastModifiedDateDesc();

}
