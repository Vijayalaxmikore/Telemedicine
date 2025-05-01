package com.example.telemedicine.controller;

import com.example.telemedicine.service.ChatService;
import com.example.telemedicine.dto.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String userMessage) {
        try {
            String geminiResponse = chatService.getGeminiResponse(userMessage);
            return ResponseEntity.ok(geminiResponse);
        } catch (Exception e) {
            // Proper error handling, including specific exception types, is crucial
            //  For example: catching IOException, HttpClientErrorException, etc.
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Gemini API: " + e.getMessage());
        }
    }

    //Example for handling different models and/or parameters
    @PostMapping("/with-options")
    public ResponseEntity<String> chatWithOptions(@RequestBody ChatRequest chatRequest) {
        try {
            String geminiResponse = chatService.getGeminiResponseWithOptions(chatRequest);
            return ResponseEntity.ok(geminiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Gemini API: " + e.getMessage());
        }
    }
}
