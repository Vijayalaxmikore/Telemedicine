package com.example.telemedicine.service;

import com.example.telemedicine.dto.ChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service
public class ChatService {

    @Value("${gemini.api.key}") // Load the API key from application.properties or environment variables
    private String geminiApiKey;

    @Value("${gemini.api.url}") // Load URL from a property file
    private String geminiApiUrl;   //e.g  "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={apiKey}"



    public String getGeminiResponse(String userMessage) {
        String url = geminiApiUrl.replace("{model}", "gemini-1.5-flash").replace("{apiKey}", geminiApiKey);

        // Construct the request body
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = List.of(Map.of("text", userMessage));
        content.put("parts", parts);
        requestBody.put("contents", List.of(content));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        // Make the API call
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);


        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Handle error responses (e.g., 400, 401, 429, 500)
            throw new RuntimeException("Gemini API call failed with status code: " + response.getStatusCodeValue());
        }
    }


    public String getGeminiResponseWithOptions(ChatRequest chatRequest) {
        // Build the URL, handling the model dynamically.
        String url = geminiApiUrl.replace("{model}", chatRequest.getModel()).replace("{apiKey}", geminiApiKey);


        // Construct the request body, handling optional parameters.
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = List.of(Map.of("text", chatRequest.getMessage()));
        content.put("parts", parts);
        requestBody.put("contents", List.of(content));

        Map<String, Object> generationConfig = new HashMap<>();
        if (chatRequest.getTemperature() != null) {
            generationConfig.put("temperature", chatRequest.getTemperature());
            //  Add other generation config parameters as needed
        }
        if (!generationConfig.isEmpty()) {
            requestBody.put("generationConfig", generationConfig);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gemini API call failed with status code: " + response.getStatusCodeValue());
        }
    }
}