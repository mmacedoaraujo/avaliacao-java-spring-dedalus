package com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain.HttpResponse;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler {

    private static final String NOT_VALID_ID = "Número de ID inválido, verifique e tente novamente.";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundHandler() {
        return createHttpResponse(HttpStatus.NOT_FOUND, NOT_VALID_ID);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> HttpRequestMethodNotSupportedHandler() {
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado, por favor verifique-o e tente novamente");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HttpResponse> methodArgumentTypeMismatchHandler() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, NOT_VALID_ID);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return new ResponseEntity<>(new HttpResponse(timestamp, httpStatus.value(), httpStatus.getReasonPhrase(), message), httpStatus);
    }


}
