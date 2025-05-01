package com.example.telemedicine.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketHandler extends TextWebSocketHandler {

    // Store sessions by appointment ID (for simplicity in this MVP)
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection established: " + session.getId()); // Add logging
        // You'd ideally get the appointmentId from the session attributes or a query parameter.
        // For this MVP, we'll just store the session directly.  This is NOT scalable.
        // sessions.put(appointmentId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload); // Add logging


        try {
            // Parse the incoming JSON message
            SignalMessage signalMessage = objectMapper.readValue(payload, SignalMessage.class);
            String appointmentId = signalMessage.getAppointmentId();

            // Store session by appointment ID.  Store *after* parsing JSON.
            if (!sessions.containsKey(appointmentId)) {
                sessions.put(appointmentId, session);
            }


            // Relay the message to the other client in the same appointment.
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                if (entry.getKey().equals(appointmentId) && !entry.getValue().equals(session)) {
                    try {
                        System.out.println("Sending message to session: " + entry.getValue().getId() + ", message: " + message.getPayload());
                        entry.getValue().sendMessage(message); // Send the *original* message
                    } catch (IOException e) {
                        System.err.println("Error sending message to session " + entry.getValue().getId() + ": " + e.getMessage());
                        // Handle errors (e.g., remove session)
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            // Handle JSON parsing or other errors.
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove session (in a real app, you'd remove based on appointmentId)
        System.out.println("WebSocket connection closed: " + session.getId() + ", status: " + status);
        sessions.values().remove(session);
    }

    // Inner class for the signal message structure.
    private static class SignalMessage {
        private String appointmentId;
        private Object signalData; // Keep this as Object

        public String getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(String appointmentId) {
            this.appointmentId = appointmentId;
        }

        public Object getSignalData() {
            return signalData;
        }

        public void setSignalData(Object signalData) {
            this.signalData = signalData;
        }
    }
}