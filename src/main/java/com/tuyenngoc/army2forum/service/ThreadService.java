package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Thread;

import java.util.List;

public interface ThreadService {

    Thread createThread(Thread thread);

    Thread updateThread(Long id, Thread thread);

    void deleteThread(Long id);

    Thread getThreadById(Long id);

    List<Thread> getAllThreads();

}
