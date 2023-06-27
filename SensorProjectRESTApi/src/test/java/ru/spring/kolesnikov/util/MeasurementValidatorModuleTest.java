package ru.spring.kolesnikov.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import ru.spring.kolesnikov.models.Measurement;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.services.SensorService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MeasurementValidatorModuleTest {

    private SensorService sensorService = Mockito.mock(SensorService.class);
    private MeasurementValidator measurementValidator = new MeasurementValidator(sensorService);
    private Optional<Sensor> optionalSensor;
    private Measurement measurement;
    private BindingResult bindingResult;

    @BeforeEach
    void beforeEach() {
        Sensor sensor = new Sensor();
        sensor.setName("Pro");
        measurement = new Measurement();
        measurement.setSensor(sensor);
        optionalSensor = Optional.of(sensor);
        bindingResult = Mockito.mock(BindingResult.class);
        //doNothing().when(bindingResult).rejectValue(any(String.class), any(String.class), any(String.class));
    }

    @Test
    void testValidate_whenHaveNotErrors_rejectValueNotInvoke() {
        // Arrange
        when(sensorService.findByName(any())).thenReturn(optionalSensor);

        // Act
        measurementValidator.validate(measurement, bindingResult);
        // Assert
        verify(bindingResult, times(0))
                .rejectValue(any(String.class), any(String.class), any(String.class));
    }

    @Test
    void testValidate_whenHaveErrors_rejectValueInvoke() {
        // Arrange
        optionalSensor = Optional.empty();
        when(sensorService.findByName(any())).thenReturn(optionalSensor);

        // Act
        measurementValidator.validate(measurement, bindingResult);

        // Assert
        verify(bindingResult, times(1))
                .rejectValue(any(String.class), any(String.class), any(String.class));
    }
}
