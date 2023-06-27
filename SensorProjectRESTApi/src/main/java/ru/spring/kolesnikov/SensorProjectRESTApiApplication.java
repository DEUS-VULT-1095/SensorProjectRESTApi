package ru.spring.kolesnikov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.spring.kolesnikov.controllers.MeasurementController;

@SpringBootApplication
public class SensorProjectRESTApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorProjectRESTApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
