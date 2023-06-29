package ru.spring.kolesnikov.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import ru.spring.kolesnikov.dto.MeasurementDTO;
import ru.spring.kolesnikov.dto.MeasurementsResponse;
import ru.spring.kolesnikov.models.Measurement;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.services.MeasurementService;
import ru.spring.kolesnikov.util.MeasurementNotAddedException;
import ru.spring.kolesnikov.util.MeasurementValidator;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = MeasurementController.class)
public class MeasurementControllerWebLayerTest {

    @MockBean
    private MeasurementService measurementService;
    @MockBean
    private MeasurementValidator measurementValidator;
    @Autowired
    private MockMvc mockMvc;
    private Measurement measurement1;
    private Measurement measurement2;
    private Measurement measurement3;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private BindingResult bindingResult;
    @Autowired
    private MeasurementController measurementController;

    @BeforeEach
    void setup() throws JsonProcessingException {
        Sensor sensor = new Sensor();
        sensor.setName("TestName");

        measurement1 = new Measurement();
        measurement1.setSensor(sensor);
        measurement1.setRaining(true);
        measurement1.setTemperature(0.0f);

        measurement2 = new Measurement();
        measurement2.setSensor(sensor);
        measurement2.setRaining(false);
        measurement2.setTemperature(1.1f);

        measurement3 = new Measurement();
        measurement3.setSensor(sensor);
        measurement3.setRaining(false);
        measurement3.setTemperature(2.1f);


        doNothing().when(measurementValidator).validate(any(Object.class), any(Errors.class));
        doNothing().when(measurementService).save(any(Measurement.class));
    }

    @Test
    void testAddMeasurement_whenProvidedValidDetails_returnsHttpStatusCodeOK() throws Exception {
        // Arrange
        MeasurementDTO measurementDTO = modelMapper.map(measurement1, MeasurementDTO.class);
        RequestBuilder request = MockMvcRequestBuilders.post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(measurementDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();


        // Assert
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Http status code should be OK.");
    }

    @Test
    void testAddMeasurement_whenBindingResultHasErrors_thrownMeasurementNotAddedException() throws Exception {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        MeasurementDTO measurementDTO = modelMapper.map(measurement1, MeasurementDTO.class);
        RequestBuilder request = MockMvcRequestBuilders.post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(measurementDTO));

        // Act

        // Assert
        Assertions.assertThrows(MeasurementNotAddedException.class,
                () -> measurementController.addMeasurement(measurementDTO, bindingResult),
                "Should be thrown MeasurementNotAddedException.");
    }

    @Test
    void testGetMeasurements_returnsMeasurementsResponse() throws Exception {
        // Arrange
        List<Measurement> measurements = new ArrayList<>(List.of(measurement1, measurement2, measurement3));

        when(measurementService.findAll()).thenReturn(measurements);

        RequestBuilder request = MockMvcRequestBuilders.get("/measurements")
                .accept(MediaType.APPLICATION_JSON);
        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String stringResult = mvcResult.getResponse().getContentAsString();
        MeasurementsResponse measurementsResponse = objectMapper.readValue(stringResult, MeasurementsResponse.class);

        // Assert
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Status code should be 200 OK");
        Assertions.assertNotNull(measurementsResponse, "Measurements response should not be Null.");
        Assertions.assertEquals(3, measurementsResponse.getMeasurementDTOList().size(),
                "Incorrect MeasurementDTOList().size().");
        Assertions.assertEquals(measurement1.getTemperature(), measurementsResponse.getMeasurementDTOList().get(0).getTemperature(),
                "Value incorrect.");
        Assertions.assertEquals(measurement2.getTemperature(), measurementsResponse.getMeasurementDTOList().get(1).getTemperature(),
                "Value incorrect.");
        Assertions.assertEquals(measurement3.getTemperature(), measurementsResponse.getMeasurementDTOList().get(2).getTemperature(),
                "Value incorrect.");
        Assertions.assertEquals(measurement1.getRaining(), measurementsResponse.getMeasurementDTOList().get(0).getRaining(),
                "Raining incorrect.");
        Assertions.assertEquals(measurement2.getRaining(), measurementsResponse.getMeasurementDTOList().get(1).getRaining(),
                "Raining incorrect.");
        Assertions.assertEquals(measurement3.getRaining(), measurementsResponse.getMeasurementDTOList().get(2).getRaining(),
                "Raining incorrect.");
        Assertions.assertEquals(measurement1.getSensor().getName(),
                measurementsResponse.getMeasurementDTOList().get(0).getSensor().getName(),
                "Sensor name incorrect.");
        Assertions.assertEquals(measurement2.getSensor().getName(),
                measurementsResponse.getMeasurementDTOList().get(1).getSensor().getName(),
                "Sensor name incorrect.");
        Assertions.assertEquals(measurement3.getSensor().getName(),
                measurementsResponse.getMeasurementDTOList().get(2).getSensor().getName(),
                "Sensor name incorrect.");
    }

    @Test
    void testGetRainyDaysCount_returnsInteger() throws Exception {
        // Arrange
        List<Measurement> measurements = new ArrayList<>(List.of(measurement1));
        when(measurementService.findByRaining(true)).thenReturn(measurements);

        RequestBuilder request = MockMvcRequestBuilders.get("/measurements/rainyDaysCount")
                .accept(MediaType.APPLICATION_JSON);
        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        Integer result = Integer.parseInt(mvcResult.getResponse().getContentAsString());
        // Assert
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Http status code should be 200 OK.");
        Assertions.assertEquals(1, result, "Rainy days count should be equals 1.");
    }
}
