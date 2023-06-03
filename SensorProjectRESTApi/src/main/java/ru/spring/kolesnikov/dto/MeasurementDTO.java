package ru.spring.kolesnikov.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull(message = "Value should not be empty")
    @Min(value = -100)
    @Max(value = 100)
    private Float value;
    @NotNull(message = "Raining value should not be empty")
    private Boolean raining;
    @NotNull
    private SensorDTO sensor;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
