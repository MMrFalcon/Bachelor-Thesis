package com.falcon.forum.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class TagNotFoundException extends RuntimeException {
    TagNotFoundException(String var1) {
        super(var1)
    }
}
