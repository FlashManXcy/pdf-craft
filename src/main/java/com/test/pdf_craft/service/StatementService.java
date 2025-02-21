package com.test.pdf_craft.service;

import com.test.pdf_craft.config.RetryRequestHolder;
import com.test.pdf_craft.model.info.RetryCallInfo;
import com.test.pdf_craft.model.info.Transaction;
import com.test.pdf_craft.model.request.TemplateEngineRequest;
import com.test.pdf_craft.model.response.CoreBankingResponse;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.utils.DateUtils;
import com.test.pdf_craft.utils.JSONUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class StatementService {
    @Resource
    private CoreBankingService coreBankingService;
    @Resource
    private TemplateEngineService templateEngineService;
    @Resource
    private RetryRequestHolder retryRequestHolder;


    @Async("asyncExecutor")
    public void processStatementAsync(String accountNumber, String fromDate, String toDate) {
        log.info("Processing statement for: {} from {} to {}", accountNumber, fromDate, toDate);
        List<Transaction> allTransactions = new ArrayList<>();
        boolean hasError = false;

        try {
            List<DateUtils.DateRange> dateRanges = DateUtils.splitDateRange(fromDate, toDate);

            outerLoop:
            for (DateUtils.DateRange range : dateRanges) {
                int currentPage = 1;
                boolean lastPage;
                do {
                    try {
                        CoreBankingResponse response = coreBankingService.getTransactions(accountNumber, range.getStartDate(),
                                range.getEndDate(),
                                currentPage
                        );
                        allTransactions.addAll(response.getTransactions());
                        lastPage = response.getPage().isLastPage();
                        currentPage++;
                    } catch (Exception e) {
                        log.error("Core banking service error: {}", e.getMessage());
                        hasError = true;
                        break outerLoop;
                    }
                } while (!lastPage);
            }

            if (!hasError && !CollectionUtils.isEmpty(allTransactions)) {
                generatePdf(allTransactions);
            }
        } catch (Exception e) {
            log.error("Processing failed: {}", e.getMessage());
            hasError = true;
        }

        if (hasError) {
            RetryCallInfo retryCallInfo = new RetryCallInfo();
            retryCallInfo.setAccountNumber(accountNumber);
            retryCallInfo.setFromDate(fromDate);
            retryCallInfo.setToDate(toDate);
            retryRequestHolder.addRequest(retryCallInfo);
        }
    }

    private void generatePdf(List<Transaction> transactions) {
        try {
            TemplateEngineRequest request = new TemplateEngineRequest();
            request.setTemplateId("AccountStatement");
            request.setData(transactions);

            TemplateEngineResponse response = templateEngineService.generatePdf(request);
            log.info("PDF generated successfully :{}", JSONUtils.beanToJson(response));
        } catch (Exception e) {
            log.error("PDF generation failed: {}", e.getMessage());
            throw new RuntimeException("PDF generation failed");
        }
    }
}