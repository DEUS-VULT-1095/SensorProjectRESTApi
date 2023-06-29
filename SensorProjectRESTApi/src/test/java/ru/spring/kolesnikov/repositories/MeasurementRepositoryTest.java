package ru.spring.kolesnikov.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.spring.kolesnikov.models.Measurement;

import java.util.List;

@DataJpaTest
public class MeasurementRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private MeasurementRepository measurementRepository;

    @BeforeEach
    void beforeEach() {
        Measurement measurement1 = new Measurement();
        measurement1.setRaining(true);
        measurement1.setTemperature(0.0f);
        testEntityManager.persistAndFlush(measurement1);

        Measurement measurement2 = new Measurement();
        measurement2.setRaining(false);
        measurement2.setTemperature(0.0f);
        testEntityManager.persistAndFlush(measurement2);

        Measurement measurement3 = new Measurement();
        measurement3.setRaining(false);
        measurement3.setTemperature(0.0f);
        testEntityManager.persistAndFlush(measurement3);
    }

    @Test
    void testFindByRaining_whenRainingIsTrue_returnsMeasurementList() {
        // Arrange

        // Act
        List<Measurement> measurements = measurementRepository.findByRaining(true);
        // Assert
        Assertions.assertNotNull(measurements, "Measurements list should not be null.");
        Assertions.assertEquals(1, measurements.size(), "Incorrect rainy days count.");
    }

    @Test
    void testFindByRaining_whenRainingIsFalse_returnsMeasurementList() {
        // Arrange

        // Act
        List<Measurement> measurements = measurementRepository.findByRaining(false);
        // Assert
        Assertions.assertNotNull(measurements, "Measurements list should not be null.");
        Assertions.assertEquals(2, measurements.size(), "Incorrect non rainy days count.");
    }
}
