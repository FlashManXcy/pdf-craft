package com.test.pdf_craft.service;

import com.test.pdf_craft.model.request.TemplateEngineRequest;
import com.test.pdf_craft.model.response.TemplateEngineResponse;
import com.test.pdf_craft.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class TemplateEngineService {

    public TemplateEngineResponse generatePdf(TemplateEngineRequest request) {
        log.info("generatePdf request:{}", JSONUtils.beanToJson(request));
        String base64Encoded = Base64.getEncoder().encodeToString(JSONUtils.beanToJson(request).getBytes());
        TemplateEngineResponse response = new TemplateEngineResponse();
        response.setData(base64Encoded);
        log.info("generatePdf response:{}", JSONUtils.beanToJson(response));
        return response;
    }
}