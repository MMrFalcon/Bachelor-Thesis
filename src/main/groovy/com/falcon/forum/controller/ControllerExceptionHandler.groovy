package com.falcon.forum.controller

import com.falcon.forum.exception.ExceptionResponse
import com.falcon.forum.exception.InactiveEntityException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import java.time.LocalDate

@ControllerAdvice
@RestController
class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InactiveEntityException.class)
    ResponseEntity<ExceptionResponse> handleInactiveEntityException(Exception ex, WebRequest request){

        ExceptionResponse exResponse = new ExceptionResponse(timestamp: LocalDate.now(), message: ex.getMessage(),
                stackTrace: ex.getStackTrace().toString(), details: request.toString()
        )

        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST)
    }
}
