package org.example.dto;


public class MeasurementDTO {
    private float temperature;
    private boolean raining;
    private SensorDTO sensor;

    public MeasurementDTO() {
    }

    public MeasurementDTO(float temperature, boolean raining, SensorDTO sensor) {
        this.temperature = temperature;
        this.raining = raining;
        this.sensor = sensor;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
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
                "Value: " + temperature + "\n" + "Raining: " + raining;
    }
}
