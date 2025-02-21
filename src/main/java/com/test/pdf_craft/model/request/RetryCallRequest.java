package com.test.pdf_craft.model.request;/**
 * @author acer
 * @date 2025-02-21 14:56
 */

import com.test.pdf_craft.model.info.RetryCallInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  14:56
 * @Description:
 * @Version: 1.0
 */
@Data
public class RetryCallRequest {
    private List<RetryCallInfo> list;
}
