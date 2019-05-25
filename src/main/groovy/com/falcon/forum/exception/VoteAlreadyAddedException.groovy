package com.falcon.forum.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class VoteAlreadyAddedException extends RuntimeException {
    VoteAlreadyAddedException(String message) {
        super(message)
    }
}
