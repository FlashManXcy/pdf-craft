package com.test.pdf_craft.job;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.service.StatementService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class RetrySchedulerJob {
    @Resource
    private StatementService statementService;
    @Resource
    private RetryRequestHolder retryRequestHolder;

    private static final int MAX_RETRY = 3;

    //每10分钟
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void retryFailedRequests() {
        List<RetryCallInfo> requests;
        synchronized (retryRequestHolder) {
            requests = retryRequestHolder.getAllRequests();
        }

        for (RetryCallInfo request : requests) {
            try {
                if (request.getRetryCount() < MAX_RETRY) {
                    statementService.processStatementAsync(
                            request.getAccountNumber(),
                            request.getFromDate(),
                            request.getToDate()
                    );
                    synchronized (retryRequestHolder) {
                        request.setRetryCount(request.getRetryCount() + 1);
                    }
                } else {
                    log.warn("Max retries reached for: {}", request);
                    synchronized (retryRequestHolder) {
                        retryRequestHolder.removeRequest(request);
                    }
                }
            } catch (Exception e) {
                log.error("Retry failed for: {}", request, e);
            }
        }
    }
}