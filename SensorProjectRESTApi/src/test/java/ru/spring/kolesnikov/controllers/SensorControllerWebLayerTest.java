package ru.spring.kolesnikov.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ru.spring.kolesnikov.dto.SensorDTO;
import ru.spring.kolesnikov.models.Sensor;
import ru.spring.kolesnikov.services.SensorService;
import ru.spring.kolesnikov.util.SensorNotCreatedException;
import ru.spring.kolesnikov.util.SensorValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SensorController.class)
public class SensorControllerWebLayerTest {
    @MockBean
    private SensorValidator sensorValidator;
    @MockBean
    private SensorService sensorService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private SensorDTO sensorDTO;
    private RequestBuilder request;
    @Autowired
    private SensorController sensorController;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        sensorDTO = new SensorDTO();
        sensorDTO.setName("T-1000");
        doNothing().when(sensorService).save(any(Sensor.class));
        doNothing().when(sensorValidator).validate(any(Sensor.class), any(BindingResult.class));
        request = MockMvcRequestBuilders.post("/sensors/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensorDTO));
    }

    @Test
    void testRegisterSensor_whenProvidedSensorDTO_returnsHttpStatusOK() throws Exception {
        // Arrange

        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        // Assert
        Assertions.assertNotNull(mvcResult.getResponse(), "Response should not be null.");
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Https Status code should be 200 OK.");
    }

    @Test
    void testRegisterSensor_whenBindingResultHasErrors_thrownSensorNotCreatedException() {
        // Arrange
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act

        // Assert
        Assertions.assertThrows(SensorNotCreatedException.class,
                () -> sensorController.registerSensor(sensorDTO, bindingResult),
                "Should be thrown SensorNotCreatedException.");
    }
}
