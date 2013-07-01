package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

	private AbstractShowcaseComponent component;

	@PostConstruct
	public void init() {
		this.activateTextComponent();
	}

	public boolean isTextComponentRendered() {
		return TextShowcaseComponent.class.equals(this.component.getClass());
	}

	public void activateTextComponent() {
		this.component = new TextShowcaseComponent();
	}

	public boolean isTextAreaComponentRendered() {
		return TextAreaShowcaseComponent.class.equals(this.component.getClass());
	}

	public void activateTextAreaComponent() {
		this.component = new TextAreaShowcaseComponent();
	}

	public boolean isComboBoxComponentRendered() {
		return ComboBoxShowcaseComponent.class.equals(this.component.getClass());
	}

	public void activateComboBoxComponent() {
		this.component = new ComboBoxShowcaseComponent();
	}

	public ComboBoxShowcaseComponent getComboBoxComponent() {
		return (ComboBoxShowcaseComponent) this.component;
	}

	public AbstractShowcaseComponent getShowcaseComponent() {
		return this.component;
	}
}
