package com.example.nutra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseLoggingAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        // Exclude Swagger/OpenAPI endpoints if any, or static resource requests if they
        // somehow hit here
        if (request.getURI().getPath().contains("/v3/api-docs") || request.getURI().getPath().contains("/swagger-ui")) {
            return body;
        }

        try {
            System.out.println("\n=======================================================");
            System.out.println("API HIT -> " + request.getMethod() + " " + request.getURI().getPath());
            System.out.println("RESPONSE DATA SENT TO FRONTEND:");

            if (body != null) {
                // Convert the response to formatted JSON for clean console reading
                String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                System.out.println(jsonResponse);
            } else {
                System.out.println("No Response Body (Empty / Void)");
            }
            System.out.println("=======================================================\n");

        } catch (Exception e) {
            System.out.println("Error logging response: " + e.getMessage());
        }

        return body;
    }
}
