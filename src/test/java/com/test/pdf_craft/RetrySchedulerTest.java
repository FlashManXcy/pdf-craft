package com.test.pdf_craft;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.job.RetryScheduler;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.service.StatementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
class RetrySchedulerTest {
    @Mock
    private RetryRequestHolder retryRequestHolder;
    @MockBean
    private StatementService statementService;
    @Mock
    private RetryScheduler retryScheduler;

    @Test
    void shouldRetryFailedRequests() {
        // 添加测试请求
        RetryCallInfo retryCallInfo = new RetryCallInfo();
        retryCallInfo.setFromDate("01-01-2024");
        retryCallInfo.setToDate("01-02-2024");
        retryCallInfo.setAccountNumber("acc456");
        retryRequestHolder.addRequest(retryCallInfo);

        // 触发定时任务
        retryScheduler.retryFailedRequests();

        // 验证是否调用了服务
        verify(statementService).processStatementAsync("acc456", "01-01-2024", "01-02-2024");
    }
}