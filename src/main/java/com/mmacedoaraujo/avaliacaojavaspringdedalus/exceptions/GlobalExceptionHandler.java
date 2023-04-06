package com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain.HttpResponse;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain.HttpResponseValidation;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NOT_VALID_ID = "the specified ID is not valid";
    private static final String BODY_REQUIREMENT_VIOLATION_MESSAGE = "body of the request does not attend the requirements, please verify.";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundHandler() {
        return createHttpResponse(HttpStatus.NOT_FOUND, NOT_VALID_ID);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpResponseValidation> ConstraintViolationHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        Set<Object> missingFields = new HashSet<>(ex.getConstraintViolations().size());
        missingFields.addAll(constraintViolations.stream()
                .map(constraintViolation -> String.format("[%s] ", constraintViolation.getPropertyPath()))
                .collect(Collectors.toList()));

        Set<Object> messages = new HashSet<>(ex.getConstraintViolations().size());
        messages.addAll(constraintViolations.stream()
                .map(constraintViolation -> String.format("[%s]", constraintViolation.getMessage()))
                .collect(Collectors.toList()));


        HttpResponseValidation response = HttpResponseValidation.builder()
                .timestamp(LocalDateTime.now())
                .error(BAD_REQUEST.getReasonPhrase())
                .status(BAD_REQUEST.value())
                .message(BODY_REQUIREMENT_VIOLATION_MESSAGE)
                .missingFields(missingFields.toString())
                .fieldsMessages(messages.toString())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());
        HttpResponse customResponse = HttpResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .build();
        return new ResponseEntity<>(customResponse, httpStatus);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message), httpStatus);
    }

}
