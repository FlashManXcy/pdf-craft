package com.test.pdf_craft.model.request;

import com.test.pdf_craft.model.info.Transaction;
import lombok.Data;

import java.util.List;
/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:34
 * @Description:
 * @Version: 1.0
 */
@Data
public class TemplateEngineRequest {
    private String templateId;
    private List<Transaction> data;
}