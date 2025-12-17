package com.pocketledger.api.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    //Success Response
    public ApiResponse(String message, T data){
        this.success = true;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    //ERROR Response
    public ApiResponse(String message){
        this.success = false;
        this.data = null;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data,String message, Boolean status){
        this.success = status;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
