package com.falcon.forum.exception

import groovy.transform.Canonical

@Canonical
class ExceptionResponse {
    private Date timestamp
    private String message
    private String stackTrace
    private String details


    Date getTimestamp() {
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


    Date setTimestamp(Date timestamp) { this.timestamp = timestamp}
    String setMessage(String message) {this.message = message}
    String setStackTrace(String stackTrace) { this.stackTrace = stackTrace}
    String setDetails(String details) { this.details = details}

}
