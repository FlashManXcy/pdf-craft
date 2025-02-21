package com.test.pdf_craft.service;

import com.test.pdf_craft.model.response.CoreBankingResponse;
import com.test.pdf_craft.model.info.PageInfo;
import com.test.pdf_craft.model.info.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:51
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class CoreBankingService {
    public CoreBankingResponse getTransactions(String accountNumber, String fromDate, String toDate, Integer pageNo) {
        log.info("accountNumber:{},fromDate:{},toDate:{},pageNo:{}", accountNumber, fromDate, toDate, pageNo);
        CoreBankingResponse response = new CoreBankingResponse();
        List<Transaction> transactions = new ArrayList<>();

        // 分页数据
        if (pageNo == 1) {
            transactions.add(createTransaction("010000032", new BigDecimal(100), "Fund transfer"));
            transactions.add(createTransaction("010000033", new BigDecimal(500), "Bill Payment"));
            response.setPage(new PageInfo(false, 1));
        } else {
            transactions.add(createTransaction("010000034", new BigDecimal(100), "Fund transfer"));
            transactions.add(createTransaction("010000035", new BigDecimal(500), "Bill Payment"));
            transactions.add(createTransaction("010000036", new BigDecimal(700), "Bill Payment"));
            transactions.add(createTransaction("010000037", new BigDecimal(800), "Fund transfer"));
            response.setPage(new PageInfo(true, 2));
        }

        response.setTransactions(transactions);
        return response;
    }

    private Transaction createTransaction(String trxRef, BigDecimal amount, String desc) {
        Transaction t = new Transaction();
        t.setTrxReferenceNo(trxRef);
        t.setValueDate("2024-11-12T00:00:00");
        t.setDescription(desc);
        t.setTrxType("D");
        t.setAmount(amount);
        t.setBeneficiaryDetails("Friends Name");
        t.setTranCurrency("AED");
        return t;
    }
}