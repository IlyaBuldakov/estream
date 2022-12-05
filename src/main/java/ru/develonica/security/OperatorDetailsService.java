package ru.develonica.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.develonica.model.repository.OperatorRepository;

@Component
@AllArgsConstructor
public class OperatorDetailsService implements UserDetailsService {

    private OperatorRepository operatorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new OperatorSecurity(operatorRepository.getByEmail(email));
    }
}