package ru.spring.kolesnikov.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMsgCreator {
    public static String createErrorMsg(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMessage.append(error.getField() + " - " + error.getDefaultMessage() + "; ");
        }
        return errorMessage.toString();
    }
}
