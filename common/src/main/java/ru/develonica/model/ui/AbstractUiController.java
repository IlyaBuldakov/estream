package ru.develonica.model.ui;


import java.io.IOException;
import javax.faces.context.FacesContext;

public abstract class AbstractUiController {

    public void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
