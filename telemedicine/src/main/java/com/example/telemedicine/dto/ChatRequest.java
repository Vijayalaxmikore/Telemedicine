package com.example.telemedicine.dto;

public class ChatRequest {
private String message;
private String model = "gemini-1.5-flash"; // Default model, can be overridden
// Add other Gemini API parameters as needed (e.g., temperature, topP, topK, etc.)
private Double temperature;


public String getMessage() {
    return message;
}

public void setMessage(String message) {
    this.message = message;
}

public String getModel() {
    return model;
}

public void setModel(String model) {
    this.model = model;
}

public Double getTemperature() {
    return temperature;
}

public void setTemperature(Double temperature) {
    this.temperature = temperature;
}

//  Getters and setters for other parameters
}