package ru.develonica.model.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import ru.develonica.model.Operator;
import ru.develonica.model.service.OperatorService;
import ru.develonica.view.managed.PanelBean;

@Component
public class SessionEndedListener implements HttpSessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(PanelBean.class);

    @Autowired
    private OperatorService operatorService;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOG.info("Сессия %s завершена".formatted(se.getSession().getId()));
        HttpSession session = se.getSession();
        SecurityContext context = (SecurityContext) session.getAttribute
                (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            Operator operator = (Operator) authentication.getPrincipal();
            this.operatorService.disableOperatorWorkSession(operator);
        }
    }
}
