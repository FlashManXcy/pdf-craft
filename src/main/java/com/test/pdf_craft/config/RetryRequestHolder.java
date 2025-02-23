package com.test.pdf_craft.config;

import com.test.pdf_craft.model.info.RetryCallInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class RetryRequestHolder {
    private final List<RetryCallInfo> requests = new CopyOnWriteArrayList<>();

    public void addRequest(RetryCallInfo request) {
        // 判断请求是否已经存在
        if (!containsRequest(request)) {
            requests.add(request);
        }
    }

    public List<RetryCallInfo> getAllRequests() {
        return new ArrayList<>(requests);
    }

    public void removeRequest(RetryCallInfo request) {
        requests.remove(request);
    }

    /**
     * 判断请求是否已经存在
     *
     * @param request 待判断的请求
     * @return 如果存在返回 true，否则返回 false
     */
    private boolean containsRequest(RetryCallInfo request) {
        for (RetryCallInfo existingRequest : requests) {
            if (isSameRequest(existingRequest, request)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个请求是否相同
     *
     * @param request1 第一个请求
     * @param request2 第二个请求
     * @return 如果相同返回 true，否则返回 false
     */
    private boolean isSameRequest(RetryCallInfo request1, RetryCallInfo request2) {
        return request1.getAccountNumber().equals(request2.getAccountNumber()) &&
                request1.getFromDate().equals(request2.getFromDate()) &&
                request1.getToDate().equals(request2.getToDate());
    }
}