package Falcon.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserNotFoundException extends RuntimeException {
    UserNotFoundException(String msg) {
        super(msg)
    }
}
