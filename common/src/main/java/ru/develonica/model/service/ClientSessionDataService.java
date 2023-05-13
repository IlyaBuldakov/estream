package ru.develonica.model.service;

import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.Operator;

/**
 * Класс для определения информации о клиенте в рамках текущей сессии.
 * <p>
 * Обеспечивает доступ к объекту оператора в рамках активной сессии,
 * предотвращая повторное обращение к {@link SecurityContext}.
 */
@SessionScope
@Component
public class ClientSessionDataService {

    /**
     * Объект оператора актуальный для текущей сессии.
     * <p>
     * Optional empty означает, что ещё не определено, является ли
     * клиент с текущей сессией оператором.
     */
    private final Optional<Operator> currentSessionOperator = this.getOperatorFromContext();

    private Optional<Operator> getOperatorFromContext() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        } else {
            return Optional.of((Operator) authentication.getPrincipal());
        }
    }

    public Optional<Operator> getCurrentSessionOperator() {
        return currentSessionOperator;
    }

    public void refreshOperatorInSecurityContext(Consumer<Operator> consumer) {
        consumer.accept(this.currentSessionOperator.get());
    }
}
