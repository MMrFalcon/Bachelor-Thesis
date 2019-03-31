package Falcon.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InactiveEntityException extends RuntimeException { //FIXME
    InactiveEntityException(String s) { super(s) }
}
