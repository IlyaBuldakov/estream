package ru.develonica.model.ui;

import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@ManagedBean(name = "serveUiController")
@SessionScope
@Component
public class ServeUiController extends AbstractUiController {

    private boolean isOperator;

    public void setIsOperator(boolean isOperator) {
        this.isOperator = isOperator;
    }

    public boolean isOperator() {
        return this.isOperator;
    }

    public void stopServing() {
        if (this.isOperator) {
            super.redirect("panel");
        } else {
            super.redirect("/");
        }
    }
}