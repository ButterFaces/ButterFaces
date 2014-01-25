package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

    @Inject
    private ComboBoxShowcaseComponent comboBoxShowcaseComponent;

    @Inject
    private CheckBoxShowcaseComponent checkBoxShowcaseComponent;

    @Inject
    private TextAreaShowcaseComponent textAreaShowcaseComponent;

    @Inject
    private TextShowcaseComponent textShowcaseComponent;

    @Inject
    private SecretShowcaseComponent secretShowcaseComponent;

    private AbstractShowcaseComponent component;

    @PostConstruct
    public void init() {
        this.activateTextComponent();
    }

    public void submit() {

    }

    public boolean isTextComponentRendered() {
        return this.textShowcaseComponent.equals(this.component);
    }

    public void activateTextComponent() {
        this.component = this.textShowcaseComponent;
    }

    public boolean isSecretComponentRendered() {
        return this.secretShowcaseComponent.equals(this.component);
    }

    public void activateSecretComponent() {
        this.component = this.secretShowcaseComponent;
    }

    public boolean isTextAreaComponentRendered() {
        return this.textAreaShowcaseComponent.equals(this.component);
    }

    public void activateTextAreaComponent() {
        this.component = this.textAreaShowcaseComponent;
    }

    public boolean isComboBoxComponentRendered() {
        return this.comboBoxShowcaseComponent.equals(this.component);
    }

    public void activateComboBoxComponent() {
        this.component = this.comboBoxShowcaseComponent;
    }

    public boolean isCheckBoxComponentRendered() {
        return this.checkBoxShowcaseComponent.equals(this.component);
    }

    public void activateCheckBoxComponent() {
        this.component = this.checkBoxShowcaseComponent;
    }

    public AbstractShowcaseComponent getShowcaseComponent() {
        return this.component;
    }
}
