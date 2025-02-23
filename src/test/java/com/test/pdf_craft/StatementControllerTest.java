package com.test.pdf_craft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.pdf_craft.controller.StatementController;
import com.test.pdf_craft.model.info.Transaction;
import com.test.pdf_craft.model.request.TemplateEngineRequest;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.service.StatementService;
import com.test.pdf_craft.service.TemplateEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class StatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StatementService statementService;

    @Mock
    private TemplateEngineService templateEngineService;

    @Autowired
    private ObjectMapper objectMapper;

    private TemplateEngineRequest templateEngineRequest;

    @BeforeEach
    public void setUp() {
        templateEngineRequest = new TemplateEngineRequest();
        // 可以根据需要初始化 templateEngineRequest 的属性
    }

    @Test
    public void testProcessStatementAsync() throws Exception {
        String accountNumber = "acc123";
        String fromDate = "2024-01-01";
        String toDate = "2024-06-30";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/statements")
                        .param("accountNumber", accountNumber)
                        .param("fromDate", fromDate)
                        .param("toDate", toDate))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        //TimeUnit.SECONDS.sleep(120);
        verify(statementService, times(0)).processStatementAsync(accountNumber, fromDate, toDate);
    }

    @Test
    public void testGeneratePdf() throws Exception {
        TemplateEngineResponse response = new TemplateEngineResponse();
        response.setData("base64pdf");
        when(templateEngineService.generatePdf(any(TemplateEngineRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/generatePdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEngineRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(templateEngineService, times(0)).generatePdf(templateEngineRequest);
    }
}