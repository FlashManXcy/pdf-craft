package com.test.pdf_craft;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.job.RetryScheduler;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.service.StatementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RetrySchedulerTest {

    @Mock
    private RetryRequestHolder retryRequestHolder;

    @Mock
    private StatementService statementService;

    @Autowired
    private RetryScheduler retryScheduler;

    @Test
    public void testShouldRetryFailedRequests() {
        RetryCallInfo retryCallInfo = new RetryCallInfo();
        retryCallInfo.setFromDate("01-01-2024");
        retryCallInfo.setToDate("01-02-2024");
        retryCallInfo.setAccountNumber("acc456");

        retryScheduler.retryFailedRequests();

        Mockito.verify(statementService).processStatementAsync("acc456", "2024-01-01", "2024-12-12");
    }
}