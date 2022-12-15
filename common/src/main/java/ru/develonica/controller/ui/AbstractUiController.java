package ru.develonica.controller.ui;


import java.io.IOException;
import javax.faces.context.FacesContext;
import ru.develonica.model.exception.ApplicationException;

/**
 * Абстрактный класс UI контроллера.
 */
public abstract class AbstractUiController {

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
