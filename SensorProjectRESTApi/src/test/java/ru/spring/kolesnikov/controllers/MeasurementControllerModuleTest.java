package ru.spring.kolesnikov.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.spring.kolesnikov.dto.MeasurementDTO;
import ru.spring.kolesnikov.dto.SensorDTO;
import ru.spring.kolesnikov.models.Measurement;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.services.MeasurementService;
import ru.spring.kolesnikov.util.ErrorResponse;
import ru.spring.kolesnikov.util.MeasurementNotAddedException;
import ru.spring.kolesnikov.util.MeasurementValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MeasurementControllerModuleTest {
    private MeasurementController measurementController;
    @MockBean
    private MeasurementService measurementService;
    @MockBean
    private MeasurementValidator measurementValidator;
    private static Method methodConvertToMeasurementDTO;
    private static Method methodConvertToMeasurement;
    private MeasurementDTO measurementDTO;
    private SensorDTO sensorDTO;
    private ModelMapper modelMapper;

    public MeasurementControllerModuleTest() {
        modelMapper = new ModelMapper();
        this.measurementController = new MeasurementController(measurementService, modelMapper, measurementValidator);
    }

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        methodConvertToMeasurement = MeasurementController.class.getDeclaredMethod("convertToMeasurement",
                MeasurementDTO.class);
        methodConvertToMeasurement.setAccessible(true);

        methodConvertToMeasurementDTO = MeasurementController.class.getDeclaredMethod("convertToMeasurementDTO",
                Measurement.class);
        methodConvertToMeasurementDTO.setAccessible(true);
    }

    @BeforeEach
    void beforeEach() {
        sensorDTO = new SensorDTO();
        sensorDTO.setName("Pro");

        measurementDTO = new MeasurementDTO();
        measurementDTO.setRaining(true);
        measurementDTO.setSensor(sensorDTO);
        measurementDTO.setValue(0.0f);
    }

    @Test
    void testConvertToMeasurement_whenProvidedMeasurementDTO_returnsMeasurement() throws InvocationTargetException, IllegalAccessException {
        // Arrange

        // Act
        Measurement measurement = (Measurement) methodConvertToMeasurement.invoke(measurementController, measurementDTO);

        // Assert
        Assertions.assertNotNull(measurement, "Measurement should not be null.");
        Assertions.assertEquals(sensorDTO.getName(), measurement.getSensor().getName(),
                "Sensor name incorrect.");
        Assertions.assertEquals(measurementDTO.getValue(), measurement.getValue(),"Value incorrect.");
        Assertions.assertEquals(measurementDTO.getRaining(), measurement.getRaining(),
                "Raining status incorrect.");
    }

    @Test
    void testConvertToMeasurement_whenProvidedNull_thrownException() throws NoSuchMethodException {
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {methodConvertToMeasurement.invoke(measurementController,
                        null);}, "Should be return Exception.");
    }

    @Test
    void testConvertToMeasurementDTO_whenProvidedMeasurement_returnsMeasurementDTO() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // Arrange
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);

        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);

        // Act
        MeasurementDTO measurementDTO = (MeasurementDTO) methodConvertToMeasurementDTO.invoke(measurementController,
                measurement);

        // Assert
        Assertions.assertNotNull(measurementDTO, "MeasurementDTO should not be null.");
        Assertions.assertEquals(sensor.getName(), measurementDTO.getSensor().getName(),
                "SensorDTO name incorrect.");
        Assertions.assertEquals(measurement.getValue(), measurementDTO.getValue(),"Value incorrect.");
        Assertions.assertEquals(measurement.getRaining(), measurementDTO.getRaining(),
                "Raining status incorrect.");
    }

    @Test
    void testConvertToMeasurementDTO_whenProvidedNull_thrownException() throws NoSuchMethodException {
        // Arrange

        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {methodConvertToMeasurementDTO.invoke(measurementController,
                        null);}, "Should be return Exception.");
    }

    @Test
    void testHandleException_whenProvidedMeasurementNotAddedException_returnsErrorResponse() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // Arrange
        MeasurementNotAddedException ex = new MeasurementNotAddedException("test");
        Method method = MeasurementController.class.getDeclaredMethod("handleException",
                MeasurementNotAddedException.class);
        method.setAccessible(true);

        // Act
        ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>) method.invoke(measurementController, ex);
        ErrorResponse errorResponse = response.getBody();
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Http Status code should be BAD_REQUEST.");
        Assertions.assertEquals(ex.getMessage(), errorResponse.getMessage(), "Error message incorrect.");
        Assertions.assertNotNull(errorResponse.getLocalDateTime(), "Local Date Time should not be null.");
    }
}
