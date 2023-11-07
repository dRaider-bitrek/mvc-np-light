package com.example.mvcnplight.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

@Repository
@Slf4j
public class SemaphoreRepository {
    private ConcurrentMap<String, Semaphore> semaphoreCache = new ConcurrentHashMap<>();

    public void acquire(String imei) throws InterruptedException, TimeoutException {
        if (!semaphoreCache.containsKey(imei)) semaphoreCache.putIfAbsent(imei, new Semaphore(1, true));
        var semaphore = semaphoreCache.get(imei);
        log.info(imei+": getQueueLength "+ semaphore.getQueueLength());
        if (semaphore.getQueueLength() > 5) throw new TimeoutException();
        semaphore.acquire();
    }

   public void release(String imei) {
        if (semaphoreCache.containsKey(imei)) {
            semaphoreCache.get(imei).release();
            if ( semaphoreCache.get(imei).getQueueLength() == 0) semaphoreCache.remove(imei);
        }
    }

}
