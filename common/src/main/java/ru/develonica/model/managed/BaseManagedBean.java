package ru.develonica.model.managed;


import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.exception.ApplicationException;
import ru.develonica.model.service.ClientRequestDataService;

/**
 * Базовый класс для managed бинов.
 */
@ManagedBean(name = "baseManagedBean")
@SessionScope
@Component
public class BaseManagedBean {

    /**
     * Класс, позволяющий определить данные о клиенте в рамках запроса.
     */
    @Autowired
    private ClientRequestDataService clientRequestDataService;

    /**
     * Метод определения аутентифицирован ли пользователь.
     *
     * Это нужно для отображения соответствующих компонентов представления,
     * доступных только операторам.
     *
     * @return Boolean - является ли пользователь оператором.
     */
    public boolean isAuthenticated() {
        return this.clientRequestDataService.isAuthenticated();
    }

    /**
     * Метод редиректа.
     *
     * @param path Путь, на который нужно сделать redirect.
     */
    public void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException e) {
            throw new ApplicationException("Ошибка ввода/вывода.");
        }
    }
}
