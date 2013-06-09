package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

	private boolean readonlyTextComponent;
	private boolean requiredTextComponent;
	private boolean floatingTextComponent;
	private String labelTextComponent = "label";
	private String valueTextComponent = "value";

	public boolean isReadonlyTextComponent() {
		return this.readonlyTextComponent;
	}

	public void setReadonlyTextComponent(final boolean readonlyTextComponent) {
		this.readonlyTextComponent = readonlyTextComponent;
	}

	public boolean isRequiredTextComponent() {
		return this.requiredTextComponent;
	}

	public void setRequiredTextComponent(final boolean requiredTextComponent) {
		this.requiredTextComponent = requiredTextComponent;
	}

	public boolean isFloatingTextComponent() {
		return this.floatingTextComponent;
	}

	public void setFloatingTextComponent(final boolean floatingTextComponent) {
		this.floatingTextComponent = floatingTextComponent;
	}

	public String getLabelTextComponent() {
		return this.labelTextComponent;
	}

	public void setLabelTextComponent(final String labelTextComponent) {
		this.labelTextComponent = labelTextComponent;
	}

	public String getValueTextComponent() {
		return this.valueTextComponent;
	}

	public void setValueTextComponent(final String valueTextComponent) {
		this.valueTextComponent = valueTextComponent;
	}

	public String getTextComponentCode() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<l:text \n");
		sb.append("test");
		return sb.toString();
	}
}
