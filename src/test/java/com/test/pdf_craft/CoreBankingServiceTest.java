package com.test.pdf_craft;

import com.test.pdf_craft.model.info.PageInfo;
import com.test.pdf_craft.model.info.Transaction;
import com.test.pdf_craft.model.response.CoreBankingResponse;
import com.test.pdf_craft.service.CoreBankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoreBankingServiceTest {

    private CoreBankingService coreBankingService;

    @BeforeEach
    public void setUp() {
        coreBankingService = new CoreBankingService();
    }

    @Test
    public void testGetTransactionsPage1() throws InterruptedException {
        // 模拟 Thread.sleep 方法，避免实际延迟
        Mockito.doNothing().when(Mockito.mock(Thread.class)).sleep(20 * 1000);

        String accountNumber = "123456";
        String fromDate = "2024-01-01";
        String toDate = "2024-12-31";
        Integer pageNo = 1;

        CoreBankingResponse response = coreBankingService.getTransactions(accountNumber, fromDate, toDate, pageNo);

        assertNotNull(response);
        List<Transaction> transactions = response.getTransactions();
        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        PageInfo pageInfo = response.getPage();
        assertNotNull(pageInfo);
        assertFalse(pageInfo.isLastPage());
        assertEquals(1, pageInfo.getCurrentPage());
    }

    @Test
    public void testGetTransactionsPage2() throws InterruptedException {
        // 模拟 Thread.sleep 方法，避免实际延迟
        Mockito.doNothing().when(Mockito.mock(Thread.class)).sleep(20 * 1000);

        String accountNumber = "123456";
        String fromDate = "2024-01-01";
        String toDate = "2024-12-31";
        Integer pageNo = 2;

        CoreBankingResponse response = coreBankingService.getTransactions(accountNumber, fromDate, toDate, pageNo);

        assertNotNull(response);
        List<Transaction> transactions = response.getTransactions();
        assertNotNull(transactions);
        assertEquals(4, transactions.size());

        PageInfo pageInfo = response.getPage();
        assertNotNull(pageInfo);
        assertTrue(pageInfo.isLastPage());
        assertEquals(2, pageInfo.getCurrentPage());
    }
}