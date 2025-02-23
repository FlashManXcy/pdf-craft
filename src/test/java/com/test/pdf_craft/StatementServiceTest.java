package com.test.pdf_craft;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.job.RetrySchedulerJob;
import com.test.pdf_craft.model.info.PageInfo;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.model.info.Transaction;
import com.test.pdf_craft.model.response.CoreBankingResponse;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.service.CoreBankingService;
import com.test.pdf_craft.service.StatementService;
import com.test.pdf_craft.service.TemplateEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {
    @Mock
    private CoreBankingService coreBankingService;
    @Mock
    private TemplateEngineService templateEngineService;
    @InjectMocks
    private StatementService statementService;

    @Mock
    private RetryRequestHolder retryRequestHolder;
    @Mock
    private RetrySchedulerJob retrySchedulerJob;


    @Test
    void testProcessStatementAsync() throws InterruptedException {
        // 模拟分页响应
        when(coreBankingService.getTransactions(any(), any(), any(), eq(1)))
                .thenReturn(createCoreResponse(1, false));
        when(coreBankingService.getTransactions(any(), any(), any(), eq(2)))
                .thenReturn(createCoreResponse(2, true));

        TemplateEngineResponse mockResponse = new TemplateEngineResponse();
        mockResponse.setData("base64pdf");
        when(templateEngineService.generatePdf(any())).thenReturn(mockResponse);

        statementService.processStatementAsync("acc123", "2024-01-01", "2024-06-30");
        Thread.sleep(1000); // 等待异步完成

        verify(coreBankingService, times(12)).getTransactions(any(), any(), any(), anyInt());
        verify(templateEngineService).generatePdf(any());
    }

    private CoreBankingResponse createCoreResponse(int pageNo, boolean lastPage) {
        CoreBankingResponse response = new CoreBankingResponse();
        response.setTransactions(Arrays.asList(new Transaction(), new Transaction()));
        response.setPage(new PageInfo(lastPage, pageNo));
        return response;
    }


    @Test
    void shouldAddToRetryQueueWhenExceptionOccurs() {
        lenient().when(coreBankingService.getTransactions(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt()))
                .thenThrow(new RuntimeException("Service unavailable"));

        statementService.processStatementAsync("acc123", "01-01-2024", "31-12-2024");

        // 验证是否加入重试队列
        ArgumentCaptor<RetryCallInfo> captor = ArgumentCaptor.forClass(RetryCallInfo.class);
        Mockito.verify(retryRequestHolder).addRequest(captor.capture());

        RetryCallInfo captured = captor.getValue();
        assertEquals("acc123", captured.getAccountNumber());
        assertEquals("01-01-2024", captured.getFromDate());
    }
}