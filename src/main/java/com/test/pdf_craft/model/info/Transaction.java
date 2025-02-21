package com.test.pdf_craft.model.info;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:40
 * @Description:
 * @Version: 1.0
 */
@Data
public class Transaction {
    private String trxReferenceNo;
    private String valueDate;
    private String description;
    private String trxType;
    private BigDecimal amount;
    private String beneficiaryDetails;
    private String tranCurrency;
}


