package ru.spring.kolesnikov.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.kolesnikov.dto.MeasurementDTO;
import ru.spring.kolesnikov.dto.MeasurementsResponse;
import ru.spring.kolesnikov.models.Measurement;
import ru.spring.kolesnikov.services.MeasurementService;
import ru.spring.kolesnikov.util.ErrorResponse;
import ru.spring.kolesnikov.util.MeasurementNotAddedException;
import ru.spring.kolesnikov.util.MeasurementValidator;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static ru.spring.kolesnikov.util.ErrorMsgCreator.createErrorMsg;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper,
                                 MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        Measurement measurement = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurement, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new MeasurementNotAddedException(createErrorMsg(bindingResult));
        }

        measurementService.save(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<MeasurementsResponse> getMeasurements() {
        MeasurementsResponse measurementsResponse = new MeasurementsResponse();
        measurementsResponse.setMeasurementDTOList(measurementService.findAll().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList()));
        return new ResponseEntity<>(measurementsResponse, HttpStatus.OK);
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Integer> getRainyDaysCount() {
        Integer count = measurementService.findByRaining(true).size();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotAddedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
