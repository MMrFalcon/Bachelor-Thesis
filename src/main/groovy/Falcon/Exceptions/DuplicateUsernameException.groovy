package Falcon.Exceptions

import org.postgresql.util.PSQLException
import org.postgresql.util.PSQLState
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DuplicateUsernameException extends PSQLException {
    DuplicateUsernameException(String msg, PSQLState state) {
        super(msg, state)
    }
}
