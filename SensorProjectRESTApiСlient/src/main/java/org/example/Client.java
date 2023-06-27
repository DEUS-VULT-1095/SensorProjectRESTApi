package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.MeasurementDTO;
import org.example.dto.SensorDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class Client {
    private static String sensorName = "T 800";

    public static void main(String[] args) {
        SensorDTO sensor = registerNewSensor(sensorName);

        sendRandomMeasurement(sensor, 10);
    }

    private static SensorDTO registerNewSensor(String name)  {
        SensorDTO sensor = new SensorDTO(name);
        String url = "http://localhost:8080/sensors/registration";

        System.out.println("***** Sensor registration *****");

        makeJsonRequest(url, sensor);

        System.out.println("sensor " + name + " was register");

        return sensor;
    }

    private static void sendRandomMeasurement(SensorDTO sensor, int count)  {
        Random random = new Random();

        System.out.println("***** Adding measurement(s) *****");
        for (int i = 0; i < count; i++) {
            float value = random.nextFloat(-10, 5);
            MeasurementDTO measurement = new MeasurementDTO(value, (int) value % 3 == 0, sensor);

            makeJsonRequest("http://localhost:8080/measurements/add", measurement);
        }

        System.out.println(count + (count > 1 ? " measurements" : " measurement") + " added");
    }

    private static void makeJsonRequest(String url, Object object) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonData = mapper.writeValueAsString(object);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

            restTemplate.postForObject(url, request, HttpStatus.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
