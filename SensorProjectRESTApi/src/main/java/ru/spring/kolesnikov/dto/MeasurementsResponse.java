package ru.spring.kolesnikov.dto;

import java.util.List;

public class MeasurementsResponse {
    private List<MeasurementDTO> measurementDTOList;

    public List<MeasurementDTO> getMeasurementDTOList() {
        return measurementDTOList;
    }

    public void setMeasurementDTOList(List<MeasurementDTO> measurementDTOList) {
        this.measurementDTOList = measurementDTOList;
    }
}
