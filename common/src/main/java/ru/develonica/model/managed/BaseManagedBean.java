package ru.develonica.model.managed;


import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.exception.ApplicationException;
import ru.develonica.model.service.ClientSessionDataService;

/**
 * Базовый класс для managed бинов.
 */
@ManagedBean(name = "baseManagedBean")
@SessionScope
@Component
public class BaseManagedBean {

    @Autowired
    private ClientSessionDataService clientSessionDataService;

    /**
     * Метод определения аутентифицирован ли пользователь.
     * <p>
     * Это нужно для отображения соответствующих компонентов представления,
     * доступных только операторам.
     *
     * @return Boolean - является ли пользователь оператором.
     */
    public boolean isAuthenticated() {
        return this.clientSessionDataService.getOperatorFromContext().isPresent();
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
