package de.larmic.jsf2.component.html;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;

public abstract class AbstractHtmlContainer extends javax.faces.component.UIPanel {

	protected static final String PROPERTY_LABEL = "label";
	protected static final String PROPERTY_STYLE = "style";
	protected static final String PROPERTY_STYLE_CLASS = "styleClass";
	protected static final String PROPERTY_READONLY = "readonly";

	protected final UIInput inputComponent;

	public AbstractHtmlContainer(final String rendererType) {
		this.setRendererType(rendererType);

		this.inputComponent = this.initInputComponent();

		this.getChildren().add(this.inputComponent);
	}

	/**
	 * Returns initialized input component.
	 */
	protected abstract UIInput initInputComponent();

	public Boolean isReadonly() {
		return (Boolean) this.getStateHelper().eval("readonly", "false");
	}

	public void setReadonly(final Boolean readonly) {
		this.updateStateHelper(PROPERTY_READONLY, readonly);
	}

	public String getLabel() {
		return (String) this.getStateHelper().eval(PROPERTY_LABEL);
	}

	public void setLabel(final String label) {
		this.updateStateHelper(PROPERTY_LABEL, label);
	}

	public String getStyle() {
		return (String) this.getStateHelper().eval(PROPERTY_STYLE);
	}

	public void setStyle(final String style) {
		this.updateStateHelper(PROPERTY_STYLE, style);
	}

	public String getStyleClass() {
		return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);

	}

	public void setStyleClass(final String styleClass) {
		this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
	}

	public UIInput getInputComponent() {
		return this.inputComponent;
	}

	private void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
