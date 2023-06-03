package org.example;

import org.example.dto.MeasurementDTO;
import org.example.dto.MeasurementsResponse;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataCapture {

    public static void main(String[] args) {
        List<MeasurementDTO> measurementDTOList = getAllMeasurements();

        drawChart(measurementDTOList);
    }

    private static List<MeasurementDTO> getAllMeasurements() {
        RestTemplate restTemplate = new RestTemplate();

        MeasurementsResponse response = restTemplate.getForObject("http://localhost:8080/measurements",
                MeasurementsResponse.class);

        return response.getMeasurementDTOList();
    }

    private static void drawChart(List<MeasurementDTO> measurementDTOList) {
        if (measurementDTOList.isEmpty()) {
            System.out.println("No measurements");
            return;
        }

        XYChart chart = new XYChartBuilder().title("Temperatures").xAxisTitle("X").yAxisTitle("Y").build();

        List<Integer> steps = new ArrayList<>();
        for (int i = 1; i <= measurementDTOList.size(); i++) {
            steps.add(i);
        }

        List<Float> temperatures = measurementDTOList.stream().map(m -> m.getValue()).collect(Collectors.toList());

        chart.addSeries("Temperature", steps, temperatures);

        new SwingWrapper<>(chart).displayChart();
    }
}
