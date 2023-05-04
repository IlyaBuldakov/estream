package ru.develonica.model.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Вспомогательный сервис для определения данных о клиенте.
 * Хранит объект {@link Authentication аутентификации} клиента в рамках запроса.
 */
@Component
@RequestScope
public class ClientRequestDataService {

    private final SecurityContext securityContext = SecurityContextHolder.getContext();

    /**
     * Поле с объектом аутентификации клиента.
     * <p>
     * Тип аутентификации будет сохранен в этом поле
     * и будет всегда актуален в рамках запроса, освобождая от
     * необходимости обращаться к {@link SecurityContext} повторно,
     * когда эта информация используется несколько раз.
     */
    private final Authentication activeClientAuthentication
            = this.securityContext.getAuthentication();
    /**
     * Метод проверки клиента на факт аутентификации.
     *
     * @return Boolean - аутентифицирован ли пользователь.
     */
    public boolean isAuthenticated() {
        return !(this.activeClientAuthentication instanceof AnonymousAuthenticationToken);
    }
}