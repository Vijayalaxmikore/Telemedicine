package com.example.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller // Use @Controller, not @RestController
public class SignalingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/video-call/{appointmentId}") // /app/video-call/123
    public void handleSignal(@DestinationVariable Long appointmentId, @Payload String message) {
        //  Send to:  /topic/appointments.123
        String destination = "/topic/appointments." + appointmentId;
        System.out.println("Sending to destination: " + destination + ", message: " + message); // Add logging
        messagingTemplate.convertAndSend(destination, message);
    }
}