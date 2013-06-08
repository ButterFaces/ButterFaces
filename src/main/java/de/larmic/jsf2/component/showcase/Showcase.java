package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

	private boolean readonlyTextComponent;

	public boolean isReadonlyTextComponent() {
		return this.readonlyTextComponent;
	}

	public void setReadonlyTextComponent(final boolean readonlyTextComponent) {
		this.readonlyTextComponent = readonlyTextComponent;
	}

}
