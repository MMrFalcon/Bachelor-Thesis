package com.falcon.forum.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class PasswordsException extends RuntimeException {
    PasswordsException(String var1) {
        super(var1)
    }
}
