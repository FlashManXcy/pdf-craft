package com.test.pdf_craft;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.job.RetrySchedulerJob;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.service.StatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RetrySchedulerJobTest {

    private RetrySchedulerJob retrySchedulerJob;

    @Mock
    private StatementService statementService;

    @Mock
    private RetryRequestHolder retryRequestHolder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        retrySchedulerJob = new RetrySchedulerJob();
        // 通过反射设置依赖
        try {
            java.lang.reflect.Field statementServiceField = RetrySchedulerJob.class.getDeclaredField("statementService");
            statementServiceField.setAccessible(true);
            statementServiceField.set(retrySchedulerJob, statementService);

            java.lang.reflect.Field retryRequestHolderField = RetrySchedulerJob.class.getDeclaredField("retryRequestHolder");
            retryRequestHolderField.setAccessible(true);
            retryRequestHolderField.set(retrySchedulerJob, retryRequestHolder);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRetryFailedRequests_ShouldRetryRequestsWithinMaxRetry() {
        // 准备测试数据
        List<RetryCallInfo> requests = new ArrayList<>();
        RetryCallInfo request1 = new RetryCallInfo();
        request1.setAccountNumber("acc1");
        request1.setFromDate("2024-01-01");
        request1.setToDate("2024-12-31");
        request1.setRetryCount(1);
        requests.add(request1);

        // 模拟 RetryRequestHolder 的 getAllRequests 方法
        when(retryRequestHolder.getAllRequests()).thenReturn(requests);

        // 调用被测试方法
        retrySchedulerJob.retryFailedRequests();

        // 验证 StatementService 的 processStatementAsync 方法是否被调用
        verify(statementService, times(1)).processStatementAsync("acc1", "2024-01-01", "2024-12-31");
        // 验证 RetryCallInfo 的重试次数是否增加
        assertEquals(2, request1.getRetryCount());
    }

    @Test
    public void testRetryFailedRequests_ShouldRemoveRequestWhenMaxRetryReached() {
        // 准备测试数据
        List<RetryCallInfo> requests = new ArrayList<>();
        RetryCallInfo request1 = new RetryCallInfo();
        request1.setAccountNumber("acc1");
        request1.setFromDate("2024-01-01");
        request1.setToDate("2024-12-31");
        request1.setRetryCount(3);
        requests.add(request1);

        // 模拟 RetryRequestHolder 的 getAllRequests 方法
        when(retryRequestHolder.getAllRequests()).thenReturn(requests);

        // 调用被测试方法
        retrySchedulerJob.retryFailedRequests();

        // 验证 StatementService 的 processStatementAsync 方法是否未被调用
        verify(statementService, never()).processStatementAsync("acc1", "2024-01-01", "2024-12-31");
        // 验证 RetryRequestHolder 的 removeRequest 方法是否被调用
        verify(retryRequestHolder, times(1)).removeRequest(request1);
    }
}