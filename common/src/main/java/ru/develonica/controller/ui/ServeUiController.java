package ru.develonica.controller.ui;

import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * UI контроллер для обработки ввода пользователя на странице обслуживания.
 */
@ManagedBean(name = "serveUiController")
@SessionScope
@Component
public class ServeUiController extends AbstractUiController {

    /**
     * Сюда устанавливается значение true в том случае если
     * объект UI контроллера сгенерирован в рамках текущей сессии
     * для оператора (см. метод {@link #setIsOperator(boolean)}).
     */
    private boolean isOperator;

    public void setIsOperator(boolean isOperator) {
        this.isOperator = isOperator;
    }

    public boolean isOperator() {
        return this.isOperator;
    }

    /**
     * Метод обрабатывающий прекращение обслуживания.
     */
    public void stopServing() {
        if (this.isOperator) {
            super.redirect("panel");
        } else {
            super.redirect("/");
        }
    }
}