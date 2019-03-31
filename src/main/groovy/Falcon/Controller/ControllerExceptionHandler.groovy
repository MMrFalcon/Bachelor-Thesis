package Falcon.Controller

import Falcon.Exceptions.ExceptionResponse
import Falcon.Exceptions.InactiveEntityException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InactiveEntityException.class)
    ResponseEntity<ExceptionResponse> handleInactiveEntityException(Exception ex, WebRequest request){

        ExceptionResponse exResponse = new ExceptionResponse(timestamp: new Date(), message: ex.getMessage(),
                stackTrace: ex.getStackTrace().toString(), details: request.toString()
        )

        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST)
    }
}
