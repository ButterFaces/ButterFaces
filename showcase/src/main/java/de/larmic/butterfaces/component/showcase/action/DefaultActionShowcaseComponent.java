package de.larmic.butterfaces.component.showcase.action;

import de.larmic.butterfaces.component.showcase.AbstractShowcaseComponent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class DefaultActionShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private String buttonClick;
    private String value;

    public void clickFirst() {
        buttonClick = "click first button";
    }

    public void clickLast() {
        buttonClick = "click last button";
    }

    public void clickDefault() {
        buttonClick = "click default button";
    }

    @Override
    public String getXHtml() {
        return null;
    }

    public String getButtonClick() {
        return buttonClick;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.buttonClick = null;
    }
}

