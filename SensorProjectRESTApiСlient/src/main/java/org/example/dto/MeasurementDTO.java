package org.example.dto;


public class MeasurementDTO {
    private float value;
    private boolean raining;
    private SensorDTO sensor;

    public MeasurementDTO() {
    }

    public MeasurementDTO(float value, boolean raining, SensorDTO sensor) {
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "Measurement from sensor - " + sensor.getName() + "\n" +
                "Value: " + value + "\n" + "Raining: " + raining;
    }
}
