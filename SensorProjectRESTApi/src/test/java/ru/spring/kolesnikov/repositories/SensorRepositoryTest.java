package ru.spring.kolesnikov.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.spring.kolesnikov.models.Sensor;

import java.util.Optional;

@DataJpaTest
public class SensorRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private SensorRepository sensorRepository;

    @Test
    void testFindByName_whenProvidedCorrectName_returnsOptionalSensor() {
        // Arrange
        Sensor sensor = new Sensor();
        sensor.setName("TestName");
        testEntityManager.persistAndFlush(sensor);

        // Act
        Optional<Sensor> storedSensor = sensorRepository.findByName(sensor.getName());

        // Assert
        Assertions.assertDoesNotThrow(() -> storedSensor.get(), "Sensor should not be null.");
        Assertions.assertEquals(sensor.getName(), storedSensor.get().getName(),
                "The returned sensor name does not match the expected value.");
    }
}
