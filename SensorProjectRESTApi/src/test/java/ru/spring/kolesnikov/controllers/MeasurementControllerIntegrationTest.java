package ru.spring.kolesnikov.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
properties = {"server.port=5432"})
public class MeasurementControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
//    @ParameterizedTest
//    @ValueSource(strings = {"-10.0", "10.0"})
    void testAddMeasurement_whenValidDetailsProvided_returnsHttpStatusCodeOK() throws JSONException {
        // Arrange
        JSONObject measurementDTORequestJason = new JSONObject();
        JSONObject sensorNestedObject = new JSONObject();
        sensorNestedObject.put("name", "Pro-11230");
        measurementDTORequestJason.put("value", "10.1");
        measurementDTORequestJason.put("raining", true);
        measurementDTORequestJason.put("sensor", sensorNestedObject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(measurementDTORequestJason.toString(), headers);

        // Act

        ResponseEntity response = testRestTemplate.postForEntity("/measurements/add", request, null);

        // Assert
        Assertions.assertNotNull(response, "Response should be not null.");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Http status code should be 200 OK.");
    }
}
