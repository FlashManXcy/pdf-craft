package com.test.pdf_craft.model.response;

import com.test.pdf_craft.model.info.PageInfo;
import com.test.pdf_craft.model.info.Transaction;
import lombok.Data;

import java.util.List;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:31
 * @Description:
 * @Version: 1.0
 */
@Data
public class CoreBankingResponse {
    private List<Transaction> transactions;
    private PageInfo page;
}