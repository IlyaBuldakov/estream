package ru.develonica.model.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Вспомогательный сервис для определения данных о клиенте.
 */
@Component
@RequestScope
public class ClientDataService {

    private final SecurityContext securityContext = SecurityContextHolder.getContext();

    private Authentication activeClientAuthentication;

    /**
     * Метод проверки клиента на факт аутентификации.
     * Хранит объект {@link Authentication аутентификации} в рамках запроса.
     *
     * @return Boolean - аутентифицирован ли пользователь.
     */
    public boolean isAuthenticated() {
        if (this.activeClientAuthentication == null) {
            this.activeClientAuthentication = this.securityContext.getAuthentication();
        }
        return !(this.activeClientAuthentication instanceof AnonymousAuthenticationToken);
    }
}