package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmailAndIsEnabledTrue(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsById(String userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndEmail(String username, String email);

    Optional<User> findByVerificationCode(String token);

    @Modifying
    @Transactional
    void deleteAllByIsEnabledFalseAndCreatedDateBefore(LocalDateTime dateTime);

}
