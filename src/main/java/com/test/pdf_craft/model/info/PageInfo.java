package com.test.pdf_craft.model.info;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:31
 * @Description:
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class PageInfo {
    private boolean lastPage;
    private Integer currentPage;
}