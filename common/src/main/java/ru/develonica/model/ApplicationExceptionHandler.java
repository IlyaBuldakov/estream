package ru.develonica.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.develonica.model.exception.ApplicationException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    public String getErrorView(ApplicationException exception) {
        LOG.error("Произошла ошибка в приложении: %s".formatted(exception.getMessage()));
        return "error.xhtml";
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public void handleUsernameNotFound(InternalAuthenticationServiceException exception) {
        LOG.warn("Ошибка авторизации: %s".formatted(exception.getMessage()));
    }
}
