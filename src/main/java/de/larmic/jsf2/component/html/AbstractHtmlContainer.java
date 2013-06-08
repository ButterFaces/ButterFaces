package de.larmic.jsf2.component.html;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;

public abstract class AbstractHtmlContainer extends javax.faces.component.UIPanel {

	protected static final String PROPERTY_LABEL = "label";
	protected static final String PROPERTY_VALUE = "value";
	protected static final String PROPERTY_STYLE = "style";
	protected static final String PROPERTY_STYLE_CLASS = "styleClass";
	protected static final String PROPERTY_READONLY = "readonly";
	protected static final String PROPERTY_REQUIRED = "required";

	protected final UIInput inputComponent;
	protected final String family;

	public AbstractHtmlContainer(final String componentType, final String rendererType) {
		this.setRendererType(rendererType);

		this.family = componentType;
		this.inputComponent = this.initInputComponent();

		this.getChildren().add(this.inputComponent);
	}

	/**
	 * Returns initialized input component.
	 */
	protected abstract UIInput initInputComponent();

	@Override
	public String getFamily() {
		return this.family;
	}

	public String getLabel() {
		return (String) this.getStateHelper().eval(PROPERTY_LABEL);
	}

	public void setLabel(final String label) {
		this.updateStateHelper(PROPERTY_LABEL, label);
	}

	public Object getValue() {
		return this.getStateHelper().eval(PROPERTY_VALUE);
	}

	public void setValue(final Object value) {
		this.updateStateHelper(PROPERTY_VALUE, value);
	}

	public Boolean isReadonly() {
		return (Boolean) this.getStateHelper().eval(PROPERTY_READONLY, "false");
	}

	public void setReadonly(final Boolean readonly) {
		this.updateStateHelper(PROPERTY_READONLY, readonly);
	}

	public Boolean isRequired() {
		return (Boolean) this.getStateHelper().eval(PROPERTY_REQUIRED, "false");
	}

	public void setRequired(final Boolean required) {
		this.updateStateHelper(PROPERTY_REQUIRED, required);
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
