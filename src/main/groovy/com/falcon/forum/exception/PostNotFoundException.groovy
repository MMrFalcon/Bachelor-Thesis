package com.falcon.forum.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PostNotFoundException extends RuntimeException {
    PostNotFoundException(String msg) {
        super(msg)
    }
}
