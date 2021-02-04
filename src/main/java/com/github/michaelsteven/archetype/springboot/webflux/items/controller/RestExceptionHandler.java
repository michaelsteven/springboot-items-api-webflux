package com.github.michaelsteven.archetype.springboot.webflux.items.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;

import com.github.michaelsteven.archetype.springboot.webflux.items.model.ApiError;


/**
 * The Class RestExceptionHandler.
 * 
 * Use this class to define exit error exception handling. The Controller Advice
 * annotation will make it available to the controller so that exceptions caught
 * below will be converted to a standard format prior to the error response
 * being returned.
 * 
 */
@ControllerAdvice(annotations = RestController.class)
@Component
public class RestExceptionHandler extends WebFluxResponseStatusExceptionHandler
{

    /**
     * Handle validation exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(ValidationException exception)
    {
        List<String> errors = new ArrayList<>();
        errors.add(exception.getLocalizedMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle constraint violation.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception)
    {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations())
        {
            errors.add(violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    
    /**
     * Handle constraint violation.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ WebExchangeBindException.class })
    public ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException exception)
    {
        List<String> errors = new ArrayList<>();
        for( FieldError fieldError : exception.getFieldErrors()) {
        	errors.add( fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed.", errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /**
     * Handle method argument type mismatch.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception)
    {
        String error = exception.getName() + " should be of type " + exception.getRequiredType().getName();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle HTTP server error exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler({ HttpServerErrorException.class })
    public ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException exception)
    {
        String error = exception.getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_GATEWAY, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all. A catch-all / fallback handler
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception exception)
    {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(),
                "error occurred");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
