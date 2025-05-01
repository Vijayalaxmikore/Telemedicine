package com.example.telemedicine.service;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.telemedicine.dto.HeartDiseaseInput;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;

@Service
public class HeartDiseasePredictionService {

    private OrtEnvironment env;
    private OrtSession session;

    @Value("${model.path}") // Load model path from application.properties or application.yml
    private String modelPath;

    @PostConstruct
    public void init() throws OrtException {
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(modelPath, new OrtSession.SessionOptions());

        // Print input and output names (for debugging)
        System.out.println("Input Names: " + session.getInputNames());
        System.out.println("Output Names: " + session.getOutputNames());
    }

    public int predict(HeartDiseaseInput input) throws OrtException {

        // 1. Preprocess the input data (CRUCIAL - Replace with your actual preprocessing)
        float[] floatInput = preprocessInput(input);

        // 2.  Create the input tensor
        long[] shape = {1, floatInput.length}; // Batch size of 1, feature length
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(floatInput), shape);

        //3. Put tensor into map
        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put(session.getInputNames().iterator().next(), inputTensor); //Assuming only one input node.


        // 4. Run the model
        try (OrtSession.Result results = session.run(inputs)) {
            OnnxValue outputValue = results.get(0);
            
            // Handle different possible output types
            if (outputValue instanceof OnnxTensor) {
                OnnxTensor tensor = (OnnxTensor) outputValue;
                Object value = tensor.getValue();
                
                // Debug output type
                System.out.println("Output type: " + value.getClass().getName());
                
                if (value instanceof float[][]) {
                    float[][] probabilities = (float[][]) value;
                    return (probabilities[0][0] > probabilities[0][1]) ? 0 : 1;
                } else if (value instanceof long[][]) {
                    long[][] predictions = (long[][]) value;
                    return (int) predictions[0][0];
                } else if (value instanceof long[]) {
                    long[] prediction = (long[]) value;
                    return (int) prediction[0];
                } else if (value instanceof float[]) {
                    float[] probabilities = (float[]) value;
                    return (probabilities[0] > 0.5f) ? 1 : 0;
                } else {
                    throw new RuntimeException("Unexpected output type: " + value.getClass().getName());
                }
            } else {
                throw new RuntimeException("Unexpected output type: " + outputValue.getType());
            }
        } finally {
            inputTensor.close();
        }
    }


    private float[] preprocessInput(HeartDiseaseInput input) {
        List<Float> features = new ArrayList<>();
        
        // Basic features
        float age = (float) input.getAge();
        float trestbps = (float) input.getTrestbps();
        
        // Feature engineering: age_trestbps interaction
        float age_trestbps = age * trestbps;
        
        // Add features in expected order
        features.add(normalizeAge(age));                    // 1. age
        features.add((float) input.getSex());               // 2. sex
        
        // 3-5. cp (one-hot encoded, 3 columns)
        features.addAll(oneHotEncode(input.getCp(), 4));
        
        features.add(normalizeTrestbps(trestbps));         // 6. trestbps
        features.add(normalizeChol((float) input.getChol())); // 7. chol
        features.add((float) input.getFbs());               // 8. fbs
        
        // 9-10. restecg (one-hot encoded, 2 columns)
        features.addAll(oneHotEncode(input.getRestecg(), 3));
        
        features.add(normalizeThalach((float) input.getThalach())); // 11. thalach
        features.add((float) input.getExang());             // 12. exang
        features.add((float) input.getOldpeak());          // 13. oldpeak
        
        // 14-15. slope (one-hot encoded, 2 columns)
        features.addAll(oneHotEncode(input.getSlope(), 3));
        
        features.add((float) input.getCa());                // 16. ca
        features.add(normalizeInteraction(age_trestbps));   // 17. age_trestbps interaction

        // Convert List<Float> to float[] 
        float[] floatArray = new float[features.size()];
        for (int i = 0; i < features.size(); i++) {
            floatArray[i] = features.get(i);
        }

        System.out.println("Number of features: " + floatArray.length);
        return floatArray;
    }

    private List<Float> oneHotEncode(int value, int numCategories) {
        List<Float> encoded = new ArrayList<>();
        for (int i = 0; i < numCategories - 1; i++) {
            encoded.add((value == i) ? 1.0f : 0.0f);
        }
        return encoded;
    }

    // Normalization helper methods
    private float normalizeAge(float age) {
        return (age - 20) / (80 - 20);
    }

    private float normalizeTrestbps(float trestbps) {
        return (trestbps - 90) / (200 - 90);
    }

    private float normalizeChol(float chol) {
        return (chol - 120) / (570 - 120);
    }

    private float normalizeThalach(float thalach) {
        return (thalach - 70) / (210 - 70);
    }

    private float normalizeInteraction(float interaction) {
        // You might need to adjust these min/max values based on your data
        float minInteraction = 20 * 90;  // min_age * min_trestbps
        float maxInteraction = 80 * 200; // max_age * max_trestbps
        return (interaction - minInteraction) / (maxInteraction - minInteraction);
    }

}
