package com.test.api.advice;

import com.test.exception.ApiError;
import com.test.exception.PersonServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class PersonApiAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        final List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        final List<String> errors = allErrors.stream().map(r -> r.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ApiError(BAD_REQUEST, "One or more field is invalid", errors));
    }

    @ExceptionHandler(PersonServiceException.class)
    public final ResponseEntity<ApiError> handlePharmacyException(PersonServiceException ex) {
        return ResponseEntity.status(ex.getCode()).body((new ApiError(ex.getCode(), ex.getMessage(), ex.getSource())));
    }


}
