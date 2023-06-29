package ru.spring.kolesnikov.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.spring.kolesnikov.dto.SensorDTO;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.services.SensorService;
import ru.spring.kolesnikov.util.ErrorResponse;
import ru.spring.kolesnikov.util.SensorNotCreatedException;
import ru.spring.kolesnikov.util.SensorValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SensorControllerModuleTest {

    private SensorController sensorController;
    private ModelMapper modelMapper;
    @MockBean
    private SensorService sensorService;
    @MockBean
    private SensorValidator sensorValidator;

    public SensorControllerModuleTest() {
        modelMapper = new ModelMapper();
        sensorController = new SensorController(sensorService, modelMapper, sensorValidator);
    }

    @Test
    void testHandleException_whenProvidedSensorNotCreatedException_returnsErrorResponse() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // Arrange
        Method method = SensorController.class.getDeclaredMethod("handleException", SensorNotCreatedException.class);
        method.setAccessible(true);
        SensorNotCreatedException ex = new SensorNotCreatedException("test");

        // Act
        ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>) method.invoke(sensorController, ex);
        ErrorResponse errorResponse = response.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Http status code should be BAD_REQUEST.");
        Assertions.assertEquals(ex.getMessage(), errorResponse.getMessage(), "Incorrect error message.");
        Assertions.assertNotNull(errorResponse.getDateTime(), "Date Time should not be null.");
    }

    @Test
    void testConvertToSensor_whenProvidedSensorDTO_returnsSensor() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("TestName");
        Method method = SensorController.class.getDeclaredMethod("convertToSensor", SensorDTO.class);
        method.setAccessible(true);
        // Act
        Sensor sensor = (Sensor) method.invoke(sensorController, sensorDTO);

        // Assert
        Assertions.assertNotNull(sensor, "Sensor should not be null.");
        Assertions.assertEquals(sensorDTO.getName(), sensor.getName(), "Incorrect sensor name.");
    }
}
