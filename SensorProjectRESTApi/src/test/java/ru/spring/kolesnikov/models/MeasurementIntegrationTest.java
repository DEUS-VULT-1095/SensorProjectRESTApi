package ru.spring.kolesnikov.models;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class MeasurementIntegrationTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private Measurement measurement;

    @BeforeEach
    void beforeEach() {
        measurement = new Measurement();
        measurement.setTemperature(0.0f);
        measurement.setRaining(true);
    }

    @Test
    void testMeasurement_whenValidDetailsProvided_shouldReturnsStoredMeasurementDetails() {
        // Arrange

        // Act
        Measurement storedMeasurement = testEntityManager.persistAndFlush(measurement);

        // Assert
        Assertions.assertTrue(storedMeasurement.getId() > 0, "Id should be greater zero.");
        Assertions.assertEquals(measurement.getTemperature(), storedMeasurement.getTemperature(), "Incorrect value.");
        Assertions.assertEquals(measurement.getRaining(), storedMeasurement.getRaining(), "Incorrect raining.");
    }

    @ParameterizedTest
    @ValueSource(floats = {-100.1f, 100.1f})
    void testMeasurement_whenTemperatureIsNotCorrect_shouldThrownException(float temperature) {
        // Arrange
        measurement.setTemperature(temperature);

        // Act

        // Assert
        Assertions.assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(measurement),
                "Should be thrown exception.");
    }

    @Test
    void testMeasurement_whenRainingIsNull_shouldThrownException() {
        // Arrange
        measurement.setRaining(null);
        // Act

        // Assert
        Assertions.assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(measurement),
                "Should be thrown exception.");
    }

}
