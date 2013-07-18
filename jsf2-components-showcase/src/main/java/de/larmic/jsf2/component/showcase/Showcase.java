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

	private AbstractShowcaseComponent component;

	@PostConstruct
	public void init() {
		this.activateTextComponent();
	}

	public void submit() {

	}

	public boolean isTextComponentRendered() {
		return TextShowcaseComponent.class.equals(this.component.getClass());
	}

	public void activateTextComponent() {
		this.component = new TextShowcaseComponent();
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
