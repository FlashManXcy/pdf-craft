package com.test.pdf_craft.model.info;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  14:57
 * @Description:
 * @Version: 1.0
 */
@Data
public class RetryCallInfo implements Serializable {
    private String accountNumber;
    private String fromDate;
    private String toDate;
    private int retryCount ;
}
