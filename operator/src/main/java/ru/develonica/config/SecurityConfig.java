package ru.develonica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.develonica.security.ApplicationAuthenticationProvider;
import ru.develonica.security.OperatorDetailsService;

/**
 * Конфигурационный класс Spring Security.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String PANEL_PATH = "/panel";

    private static final String[] AUTHENTICATED_PATHS = {PANEL_PATH, "/stat"};

    private static final String LOGIN_URL = "/login";

    private final OperatorDetailsService operatorDetailsService;

    public SecurityConfig(OperatorDetailsService operatorDetailsService) {
        this.operatorDetailsService = operatorDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(config -> config
                        .antMatchers(AUTHENTICATED_PATHS).authenticated()
                        .anyRequest().permitAll())
                .formLogin()
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .defaultSuccessUrl(PANEL_PATH)
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .userDetailsService(this.operatorDetailsService)
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        ApplicationAuthenticationProvider applicationAuthenticationProvider = new ApplicationAuthenticationProvider();
        applicationAuthenticationProvider.setUserDetailsService(this.operatorDetailsService);
        applicationAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return applicationAuthenticationProvider;
    }
}
