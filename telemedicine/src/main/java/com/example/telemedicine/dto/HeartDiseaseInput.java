// HeartDiseaseInput.java (DTO for input data)

package com.example.telemedicine.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeartDiseaseInput {
    private int age;
    private int sex;
    private int cp;
    private int trestbps;
    private int chol;
    private int fbs;
    private int restecg;
    private int thalach;
    private int exang;
    private double oldpeak;
    private int slope;
    private int ca;
    private int thal;
}