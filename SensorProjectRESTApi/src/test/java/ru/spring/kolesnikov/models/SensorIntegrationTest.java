package ru.spring.kolesnikov.models;

import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class SensorIntegrationTest {
    @Autowired
    private TestEntityManager testEntityManager;
    private Sensor sensor;

    @BeforeEach
    void beforeEach() {
        sensor = new Sensor();
        sensor.setName("TestName");
    }

    @Test
    void testSensor_whenValidDetailsProvided_shouldReturnsStoredSensorDetails() {
        // Arrange

        // Act
        Sensor storedSensor = testEntityManager.persistAndFlush(sensor);

        // Assert
        Assertions.assertTrue(storedSensor.getId() > 0, "Id should be greater zero.");
        Assertions.assertEquals(sensor.getName(), storedSensor.getName(), "Incorrect sensor name.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ww", "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"})
    void testSensor_whenNameIsNotCorrect_shouldThrownException(String name) {
        // Arrange
        sensor.setName(name);
        // Act

        // Assert
        Assertions.assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(sensor),
                "Should be thrown exception.");
    }
}
