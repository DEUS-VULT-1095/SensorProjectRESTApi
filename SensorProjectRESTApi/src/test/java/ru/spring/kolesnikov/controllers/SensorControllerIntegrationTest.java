package ru.spring.kolesnikov.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.repositories.SensorRepository;
import ru.spring.kolesnikov.services.SensorService;
import ru.spring.kolesnikov.util.SensorNotCreatedException;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SensorControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SensorRepository sensorRepository;

    @AfterEach
    void afterEach() {
        Sensor sensor = sensorService.findByName("TestName").get();
        sensorRepository.delete(sensor);
    }

    @Test
    void testRegisterSensor_whenValidDetailsProvided_returnsHttpStatusCodeOK() throws JSONException {
        // Arrange
        JSONObject sensorDTOJason = new JSONObject();
        sensorDTOJason.put("name", "TestName");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(sensorDTOJason.toString(), headers);
        // Act
        ResponseEntity response = testRestTemplate.postForEntity("/sensors/registration", request, null);
        // Assert
        Assertions.assertNotNull(response, "Response should not be null.");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Http status code should be 200 OK.");
        Assertions.assertNotNull(sensorService.findByName("TestName").get(), "Sensor should not be null.");
    }

    @Test
    @Disabled // Error
    void testRegisterSensor_whenProvidedExistingSensorDTO_thrownSensorNotCreatedException() throws JSONException {
        // Arrange
        JSONObject sensorDTOJason = new JSONObject();
        sensorDTOJason.put("name", "TestName");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(sensorDTOJason.toString(), headers);
        // Act
        testRestTemplate.postForEntity("/sensors/registration", request, null);

        //Assert
        Assertions.assertThrows(SensorNotCreatedException.class,
                () -> testRestTemplate.postForEntity("/sensors/registration", request, null),
                "Should be thrown SensorNotCreatedException.");
    }
}
