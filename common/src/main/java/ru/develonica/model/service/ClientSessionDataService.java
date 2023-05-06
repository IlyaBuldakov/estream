package ru.develonica.model.service;

import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.Operator;

@SessionScope
@Component
public class ClientSessionDataService {

    private Optional<Operator> currentSessionOperator = Optional.empty();

    public Optional<Operator> getOperatorFromContext() {
        if (this.currentSessionOperator.isEmpty()) {
            Authentication authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                this.currentSessionOperator
                        = Optional.of((Operator) authentication.getPrincipal());
            }
        }
        return this.currentSessionOperator;
    }
}