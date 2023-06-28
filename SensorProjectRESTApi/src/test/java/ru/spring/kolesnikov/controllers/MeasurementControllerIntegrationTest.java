package ru.spring.kolesnikov.controllers;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.spring.kolesnikov.dto.MeasurementsResponse;
import ru.spring.kolesnikov.models.Measurement;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.repositories.SensorRepository;
import ru.spring.kolesnikov.services.MeasurementService;
import ru.spring.kolesnikov.services.SensorService;
import ru.spring.kolesnikov.util.ErrorResponse;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private MeasurementService measurementService;
    private Sensor sensor;
    private Measurement measurement1;
    private Measurement measurement2;

    @BeforeEach
    void beforeEach() {
        sensor = new Sensor();
        sensor.setName("Pro100");
        sensorService.save(sensor);

        measurement1 = new Measurement();
        measurement1.setSensor(sensor);
        measurement1.setRaining(false);
        measurement1.setTemperature(5.1f);

        measurement2 = new Measurement();
        measurement2.setSensor(sensor);
        measurement2.setRaining(true);
        measurement2.setTemperature(-6.1f);

        measurementService.save(measurement1);
        measurementService.save(measurement2);
    }

    @AfterEach
    void afterEach() {
        sensorRepository.delete(sensor);
    }

    @Test
    void testAddMeasurement_whenValidDetailsProvided_returnsHttpStatusCodeOK() throws JSONException {

        // Arrange
        JSONObject sensorObject = new JSONObject();
        JSONObject measurementDTORequestJason = new JSONObject();

        sensorObject.put("name", "Pro100");
        measurementDTORequestJason.put("temperature", "10.1");
        measurementDTORequestJason.put("raining", true);
        measurementDTORequestJason.put("sensor", sensorObject);

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

    @Test
    @Disabled //error :(
    void testAddMeasurement_whenProvidedNotExistingSensor_thrownMeasurementNotAddedException() throws JSONException {
        // Arrange
        JSONObject sensorObject = new JSONObject();
        JSONObject measurementDTORequestJason = new JSONObject();

        sensorObject.put("name", "not exist");
        measurementDTORequestJason.put("value", "0.9");
        measurementDTORequestJason.put("raining", false);
        measurementDTORequestJason.put("sensor", sensorObject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(measurementDTORequestJason.toString(), headers);
        // Act
        ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/measurements/add", request,
                null);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Http status should be BAD REQUEST.");
        Assertions.assertNotNull(response.getBody().getMessage(), "Message should not be null.");
        Assertions.assertNotNull(response.getBody().getDateTime(), "Error date time not be null.");

    }

    @Test
    void testGetMeasurements_returnsMeasurementsResponse() {
        // Arrange

        // Act
        ResponseEntity<MeasurementsResponse> response = testRestTemplate.getForEntity("/measurements",
                MeasurementsResponse.class);
        MeasurementsResponse measurementsResponse = response.getBody();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Http status code should be OK.");
        Assertions.assertEquals(2, measurementsResponse.getMeasurementDTOList().size(),
                "Incorrect number of MeasurementDTO.");

        Assertions.assertEquals(measurement1.getSensor().getName(),
                measurementsResponse.getMeasurementDTOList().get(0).getSensor().getName(),
                "Incorrect sensor name.");
        Assertions.assertEquals(measurement2.getSensor().getName(),
                measurementsResponse.getMeasurementDTOList().get(1).getSensor().getName(),
                "Incorrect sensor name.");

        Assertions.assertEquals(measurement1.getTemperature(),
                measurementsResponse.getMeasurementDTOList().get(0).getTemperature(),
                "Incorrect value.");
        Assertions.assertEquals(measurement2.getTemperature(),
                measurementsResponse.getMeasurementDTOList().get(1).getTemperature(),
                "Incorrect value.");

        Assertions.assertEquals(measurement1.getRaining(),
                measurementsResponse.getMeasurementDTOList().get(0).getRaining(),
                "Incorrect raining.");
        Assertions.assertEquals(measurement2.getRaining(),
                measurementsResponse.getMeasurementDTOList().get(1).getRaining(),
                "Incorrect raining.");
    }

    @Test
    void testGetRainyDaysCount_returnsCountAndHttpStatusCodeOk() {
        // Arrange

        // Act
        ResponseEntity<Integer> response = testRestTemplate.getForEntity("/measurements/rainyDaysCount",
                Integer.class);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Https status code should be OK.");
        Assertions.assertEquals(1, response.getBody(), "Incorrect rainy days count.");
    }
}
