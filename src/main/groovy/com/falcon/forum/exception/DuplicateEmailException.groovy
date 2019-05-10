package com.falcon.forum.exception

import org.postgresql.util.PSQLException
import org.postgresql.util.PSQLState
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DuplicateEmailException extends PSQLException {
    DuplicateEmailException(String msg, PSQLState state) {
        super(msg, state)
    }
}
