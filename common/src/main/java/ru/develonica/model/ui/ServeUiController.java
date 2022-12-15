package ru.develonica.model.ui;

import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.service.QueuePotentialPairHolder;

@ManagedBean(name = "serveUiController")
@SessionScope
@Component
public class ServeUiController {

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    public ServeUiController(QueuePotentialPairHolder queuePotentialPairHolder) {
        this.queuePotentialPairHolder = queuePotentialPairHolder;
    }
}
