package com.ensicaen.sepa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SepaApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SepaApplicationException.class)
    public Map<String, String> handleBusinessException(SepaApplicationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorMap.put("errorMessage", ex.getMessage());
        return errorMap;
    }


}
