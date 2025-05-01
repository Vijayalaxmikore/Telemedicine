// HeartDiseaseController.java (REST Controller)

package com.example.telemedicine.controller;

import com.example.telemedicine.dto.HeartDiseaseInput;
import com.example.telemedicine.service.HeartDiseasePredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import ai.onnxruntime.OrtException;

@RestController
@RequestMapping("/api")
public class HeartDiseaseController {

    @Autowired
    private HeartDiseasePredictionService predictionService;

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody HeartDiseaseInput input) {
        try {
            int prediction = predictionService.predict(input);
            return ResponseEntity.ok(new PredictionResponse(prediction));
        } catch (OrtException e) {
            e.printStackTrace(); // Log the exception properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Prediction failed: " + e.getMessage());
        }
    }
}