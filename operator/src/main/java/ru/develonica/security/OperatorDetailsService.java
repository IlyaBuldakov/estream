package ru.develonica.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.develonica.model.repository.OperatorRepository;

/**
 * Реализация UserDetailsService, которая говорит
 * Spring Security откуда и каким образом получать оператора
 * по имени пользователя (в нашем случае по email).
 */
@Component
public class OperatorDetailsService implements UserDetailsService {

    private final OperatorRepository operatorRepository;

    public OperatorDetailsService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new OperatorDetails(operatorRepository.getByEmail(email));
    }
}