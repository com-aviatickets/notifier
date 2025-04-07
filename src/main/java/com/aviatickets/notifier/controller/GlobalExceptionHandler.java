package com.aviatickets.notifier.controller;

import com.aviatickets.notifier.controller.response.ApiFieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_LOG_MESSAGE = "Unhandled exception occurred: ";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(DEFAULT_LOG_MESSAGE, ex);

        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ApiFieldError.builder()
                        .field(fe.getField())
                        .error(fe.getDefaultMessage())
                        .code(fe.getCode())
                        .objectName(fe.getObjectName())
                        .build())
                .toList();

        var globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(ge -> ApiFieldError.builder()
                        .field(null)
                        .error(ge.getDefaultMessage())
                        .code(ge.getCode())
                        .objectName(ge.getObjectName())
                        .build())
                .toList();

        var allErrors = Stream.concat(fieldErrors.stream(), globalErrors.stream()).toList();

        return buildApiGlobalErrorResponseEntity(allErrors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildApiGlobalErrorResponseEntity(
            List<ApiFieldError> fieldErrorList, HttpStatus httpStatus
    ) {
        return new ResponseEntity<>(fieldErrorList, httpStatus);
    }
}
