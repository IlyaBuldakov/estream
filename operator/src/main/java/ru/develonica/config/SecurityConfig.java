package ru.develonica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * Конфигурационный класс Spring Security.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String PANEL_PATH = "/panel";

    private static final String[] AUTHENTICATED_PATHS = {PANEL_PATH, "/stat"};

    private static final String LOGOUT_URL = "/logout";

    private static final String LOGIN_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(config -> config
                        .antMatchers(AUTHENTICATED_PATHS).authenticated()
                        .anyRequest().permitAll())
                .logout(config -> config
                        .logoutUrl(LOGOUT_URL).permitAll()
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                .formLogin()
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .defaultSuccessUrl(PANEL_PATH)
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
