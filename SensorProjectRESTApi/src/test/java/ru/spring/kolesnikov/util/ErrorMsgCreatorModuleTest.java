package ru.spring.kolesnikov.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.mockito.Mockito.when;

public class ErrorMsgCreatorModuleTest {

    @Test
    void testCreateErrorMsg_whenProvidedBindingResult_returnsStringMessage() {
        // Arrange
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        String object = "testObject";
        String field1 = "testField1";
        String message1 = "testMessage1";
        String field2 = "testField2";
        String message2 = "testMessage2";
        FieldError fieldError1 = new FieldError(object, field1, message1);
        FieldError fieldError2 = new FieldError(object, field2, message2);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        // Act
        String errorMsg = ErrorMsgCreator.createErrorMsg(bindingResult);

        // Assert
        Assertions.assertEquals(field1 + " - " + message1 + "; " + field2 + " - " + message2 + "; ",
                errorMsg, "Incorrect error msg.");
    }
}
