package com.example.mvcnplight.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CommandRepository {

    private final SemaphoreRepository semaphoreRepository;
    Map<String, List<Queue>> messages = new ConcurrentHashMap<>();

    public void addQueue(String imei, Queue queue) throws TimeoutException, InterruptedException {
        semaphoreRepository.acquire(imei);

        if (!messages.containsKey(imei)) {
            messages.put(imei, new CopyOnWriteArrayList<>());
        }
        messages.get(imei).add(queue);
    }

    public void removeQueue(String imei, Queue queue) {

        if (messages.containsKey(imei)) {
            if (messages.get(imei).contains(queue)) {
                messages.get(imei).remove(queue);
                if (messages.get(imei).isEmpty()) {
                    messages.remove(imei);
                }
            }
        }
        semaphoreRepository.release(imei);
    }

    public void addMessage(String imei, String message) {
        log.info(imei + " : addMessage " + message);
        var queue = messages.get(imei);
        if (queue != null) {
            queue.forEach(q -> q.add(message));
        }
    }


    //    Map<String, Queue> messages = new ConcurrentHashMap<>();
//
//    public void addQueue(String imei,Queue queue){
//        messages.put(imei,queue);
//    }
//    public void removeQueue(String imei){
//        messages.remove(imei);
//    }
//
//    public void addMessage(String imei,String message){
//       var queue= messages.get(imei);
//       if (queue!=null) queue.add(message);
//    }
}
