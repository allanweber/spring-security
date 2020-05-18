package com.allanweber.api.handler;

import com.allanweber.api.handler.dto.ResponseErrorDto;
import com.allanweber.api.handler.dto.ViolationDto;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    private static final String UNEXPECTED_ERROR_HAPPENED = "Unexpected Error happened";
    private static final String CONSTRAINT_MESSAGE = "Constraints violations found.";
    private static final String NOTFOUND_MESSAGE = "Not found exception happened.";
    private static final Integer SIZE = 1;

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseErrorDto> handleClientException(HttpClientErrorException ex) {
        log.error(UNEXPECTED_ERROR_HAPPENED, ex);
        return ResponseEntity.status(ex.getStatusCode()).body(new ResponseErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseErrorDto> handleNotFoundException(NotFoundException ex) {
        log.error(NOTFOUND_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error(NOTFOUND_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(CONSTRAINT_MESSAGE, e);
        List<ViolationDto> errors = new ArrayList<>();

        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String fieldName = error.getObjectName();
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            }
            errors.add(ViolationDto.create(fieldName, error.getDefaultMessage()));
        }

        ResponseErrorDto response = new ResponseErrorDto(errors.get(0).getMessage(), errors);
        if (errors.size() > SIZE) {
            response.setMessage(CONSTRAINT_MESSAGE);
        }

        return response;
    }
}