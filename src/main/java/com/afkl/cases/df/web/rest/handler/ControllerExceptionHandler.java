package com.afkl.cases.df.web.rest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleGlobalException(Throwable t) {
        log.error("Error during process request", t);
        return new ResponseEntity<>(SERVICE_UNAVAILABLE);
    }

}
