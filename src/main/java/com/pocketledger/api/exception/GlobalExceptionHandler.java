package com.pocketledger.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pocketledger.api.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex){
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            if(errors.containsKey(fieldName)){
                errors.get(fieldName).add(errorMessage);
            }else{
                ArrayList<String> errorList = new ArrayList<>();
                errorList.add(errorMessage);
                errors.put(fieldName,errorList);
            }
        });

        ApiResponse<Map<String, List<String>>> response = new ApiResponse<>(
                "Validation Failed", // Message First
                errors,              // Data Second
                false                // Success Third
        );
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonErrors(HttpMessageNotReadableException ex) {
        if(ex.getCause() instanceof InvalidFormatException){
            InvalidFormatException iex = (InvalidFormatException) ex.getCause();

            if(iex.getTargetType().isEnum()){
                String allowedValues = java.util.Arrays.toString(iex.getTargetType().getEnumConstants());
                String message = String.format("Invalid value '%s'. Allowed values are: %s",
                        iex.getValue(), allowedValues);

                return ResponseEntity.badRequest().body(new ApiResponse<>(message, null));
            }

        }

        return ResponseEntity.badRequest().body(new ApiResponse<>("Malformed JSON request", null));
    }
}
