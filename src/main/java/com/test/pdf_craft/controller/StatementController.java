package com.test.pdf_craft.controller;

import com.test.pdf_craft.common.Result;
import com.test.pdf_craft.enums.ResultCodeEnum;
import com.test.pdf_craft.model.info.Transaction;
import com.test.pdf_craft.model.request.TemplateEngineRequest;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.service.StatementService;
import com.test.pdf_craft.service.TemplateEngineService;
import com.test.pdf_craft.utils.JSONUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:25
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class StatementController {
    @Autowired
    private StatementService statementService;
    @Autowired
    private TemplateEngineService templateEngineService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 异步处理账单生成
     *
     * @param accountNumber 账户号
     * @param fromDate      开始日期
     * @param toDate        结束日期
     */
    @PostMapping("/statements")
    public Result generateStatement(
            @RequestParam String accountNumber,
            @RequestParam String fromDate,
            @RequestParam String toDate) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(fromDate, DATE_FORMATTER);
            endDate = LocalDate.parse(toDate, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return Result.fail(ResultCodeEnum.DATE_FORMAT_INCORRECT.getCode(), ResultCodeEnum.DATE_FORMAT_INCORRECT.getMessage());
        }
        if (startDate.isAfter(endDate)) {
            return Result.fail(ResultCodeEnum.TIME_IS_INCORRECT.getCode(), ResultCodeEnum.TIME_IS_INCORRECT.getMessage());
        }
        statementService.processStatementAsync(accountNumber, fromDate, toDate);
        return Result.success();
    }

    /**
     * 生成 PDF
     *
     * @param request 模板引擎请求
     * @return 模板引擎响应
     */
    @PostMapping("/generatePdf")
    public Result generatePdf(@RequestBody TemplateEngineRequest request) {
        try {
            TemplateEngineResponse response = templateEngineService.generatePdf(request);
            log.info("PDF generated successfully :{}", JSONUtils.beanToJson(response));
            return Result.success(response.getData());
        } catch (Exception e) {
            log.error("PDF generation failed: {}", e.getMessage());
        }
        return Result.fail(ResultCodeEnum.PDF_GENERATION_FAILED.getCode(), ResultCodeEnum.PDF_GENERATION_FAILED.getMessage());

    }

}