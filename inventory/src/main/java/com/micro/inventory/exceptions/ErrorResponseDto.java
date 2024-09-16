package com.micro.inventory.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    
    private String apiPath;

    
    private HttpStatus errorCode;

    
    private String errorMessage;

    
    private LocalDateTime errorTime;

}
