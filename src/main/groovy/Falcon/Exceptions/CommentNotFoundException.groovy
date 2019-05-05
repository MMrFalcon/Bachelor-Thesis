package Falcon.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CommentNotFoundException extends RuntimeException {
    CommentNotFoundException(String msg) {
        super(msg)
    }
}
