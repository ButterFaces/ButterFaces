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

	public AbstractShowcaseComponent getShowcaseComponent() {
		return this.component;
	}
}
