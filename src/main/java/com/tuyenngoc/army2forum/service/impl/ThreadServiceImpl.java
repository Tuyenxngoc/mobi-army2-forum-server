package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Thread;
import com.tuyenngoc.army2forum.repository.ThreadRepository;
import com.tuyenngoc.army2forum.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    @Override
    public Thread createThread(Thread thread) {
        return threadRepository.save(thread);
    }

    @Override
    public Thread updateThread(Long id, Thread thread) {
        Optional<Thread> existingThread = threadRepository.findById(id);
        if (existingThread.isPresent()) {
            Thread updatedThread = existingThread.get();
            updatedThread.setTitle(thread.getTitle());
            updatedThread.setContent(thread.getContent());
            updatedThread.setCategory(thread.getCategory());
            updatedThread.setPlayer(thread.getPlayer());
            return threadRepository.save(updatedThread);
        }
        return null;
    }

    @Override
    public void deleteThread(Long id) {
        threadRepository.deleteById(id);
    }

    @Override
    public Thread getThreadById(Long id) {
        return threadRepository.findById(id).orElse(null);
    }

    @Override
    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }
}
