package com.test.pdf_craft.config;

import com.test.pdf_craft.model.info.RetryCallInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RetryRequestHolder {
    private final ConcurrentLinkedQueue<RetryCallInfo> retryQueue = new ConcurrentLinkedQueue<>();
    
    public void addRequest(RetryCallInfo request) {
        retryQueue.add(request);
    }
    
    public List<RetryCallInfo> getAllRequests() {
        return new ArrayList<>(retryQueue);
    }
    
    public void removeRequest(RetryCallInfo request) {
        retryQueue.remove(request);
    }
}