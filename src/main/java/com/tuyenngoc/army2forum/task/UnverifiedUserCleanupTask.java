package com.tuyenngoc.army2forum.task;

import com.tuyenngoc.army2forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UnverifiedUserCleanupTask {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 86400000) // 24 hours in milliseconds
    public void cleanupUnverifiedUsers() {
        userRepository.deleteAllByIsEnabledFalseAndCreatedDateBefore(LocalDateTime.now().minusHours(24));
    }

}
