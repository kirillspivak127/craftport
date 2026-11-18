package dev.craftport.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.craftport.app.ServerNotFoundException;
import dev.craftport.store.ServerConfigException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ServerNotFoundException.class)
    public ProblemDetail handleServerNotFound(ServerNotFoundException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Server not found");

        return problemDetail;
    }

    @ExceptionHandler(ServerConfigException.class)
    public ProblemDetail handleServerConfigException(ServerConfigException e) {
        log.error("server configuration error", e);

        var detailMessage = e.serverId() != null && !e.serverId().isBlank()
                ? "Configuration for server '%s' is invalid".formatted(e.serverId())
                : "Server configuration is invalid";

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detailMessage);
        problemDetail.setTitle("Server configuration error");

        return problemDetail;
    }
}
