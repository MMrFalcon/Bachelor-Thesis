package com.falcon.forum.exception

import groovy.transform.Canonical

import java.time.LocalDate

@Canonical
class ExceptionResponse {
    private LocalDate timestamp
    private String message
    private String stackTrace
    private String details


    LocalDate getTimestamp() {
        return timestamp
    }

    String getMessage() {
        return message
    }

    String getStackTrace() {
        return stackTrace
    }

    String getDetails() {
        return details
    }


    LocalDate setTimestamp(LocalDate timestamp) { this.timestamp = timestamp}
    String setMessage(String message) {this.message = message}
    String setStackTrace(String stackTrace) { this.stackTrace = stackTrace}
    String setDetails(String details) { this.details = details}

}
