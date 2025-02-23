package com.test.pdf_craft;

import com.test.pdf_craft.common.Result;
import com.test.pdf_craft.controller.StatementController;
import com.test.pdf_craft.enums.ResultCodeEnum;
import com.test.pdf_craft.model.request.TemplateEngineRequest;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.service.StatementService;
import com.test.pdf_craft.service.TemplateEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatementControllerTest {

    @Mock
    private StatementService statementService;

    @Mock
    private TemplateEngineService templateEngineService;

    @InjectMocks
    private StatementController statementController;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateStatement_ValidDates() {
        String accountNumber = "acc123";
        String fromDate = "2024-01-01";
        String toDate = "2024-12-31";

        Result result = statementController.generateStatement(accountNumber, fromDate, toDate);

        verify(statementService, times(1)).processStatementAsync(accountNumber, fromDate, toDate);
        assertEquals(ResultCodeEnum.SUCCESS.getCode(), result.getCode());
    }

    @Test
    public void testGenerateStatement_InvalidDateFormat() {
        String accountNumber = "acc123";
        String fromDate = "01/01/2024";
        String toDate = "31/12/2024";

        Result result = statementController.generateStatement(accountNumber, fromDate, toDate);

        verify(statementService, never()).processStatementAsync(accountNumber, fromDate, toDate);
        assertEquals(ResultCodeEnum.DATE_FORMAT_INCORRECT.getCode(), result.getCode());
    }

    @Test
    public void testGenerateStatement_StartDateAfterEndDate() {
        String accountNumber = "acc123";
        String fromDate = "2024-12-31";
        String toDate = "2024-01-01";

        Result result = statementController.generateStatement(accountNumber, fromDate, toDate);

        verify(statementService, never()).processStatementAsync(accountNumber, fromDate, toDate);
        assertEquals(ResultCodeEnum.TIME_IS_INCORRECT.getCode(), result.getCode());
    }

    @Test
    public void testGeneratePdf_Success() {
        TemplateEngineRequest request = new TemplateEngineRequest();
        TemplateEngineResponse response = new TemplateEngineResponse();
        response.setData("base64pdf");

        when(templateEngineService.generatePdf(request)).thenReturn(response);

        Result result = statementController.generatePdf(request);

        verify(templateEngineService, times(1)).generatePdf(request);
        assertEquals(ResultCodeEnum.SUCCESS.getCode(), result.getCode());
        assertEquals(response.getData(), result.getData());
    }

    @Test
    public void testGeneratePdf_Failure() {
        TemplateEngineRequest request = new TemplateEngineRequest();

        when(templateEngineService.generatePdf(request)).thenThrow(new RuntimeException("PDF generation failed"));

        Result result = statementController.generatePdf(request);

        verify(templateEngineService, times(1)).generatePdf(request);
        assertEquals(ResultCodeEnum.PDF_GENERATION_FAILED.getCode(), result.getCode());
    }
}