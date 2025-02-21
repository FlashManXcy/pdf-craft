package com.test.pdf_craft.controller;

import com.test.pdf_craft.common.Result;
import com.test.pdf_craft.enums.ResultCodeEnum;
import com.test.pdf_craft.service.StatementService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    @Resource
    private StatementService statementService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/statements")
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


}