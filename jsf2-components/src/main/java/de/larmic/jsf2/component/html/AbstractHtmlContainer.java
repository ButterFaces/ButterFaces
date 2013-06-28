package de.larmic.jsf2.component.html;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
public abstract class AbstractHtmlContainer extends UIInput implements ClientBehaviorHolder {

	public static final String COMPONENT_FAMILY = "de.larmic.component.family";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.BasicContainerRenderer";

	protected static final String PROPERTY_LABEL = "label";
	protected static final String PROPERTY_VALUE = "value";
	protected static final String PROPERTY_STYLE = "style";
	protected static final String PROPERTY_STYLE_CLASS = "styleClass";
	protected static final String PROPERTY_READONLY = "readonly";
	protected static final String PROPERTY_REQUIRED = "required";
	protected static final String PROPERTY_FLOATING = "floating";
	protected static final String PROPERTY_TOOLTIP = "tooltip";

	protected final UIInput inputComponent;

	public AbstractHtmlContainer() {
		this.setRendererType(RENDERER_TYPE);

		this.inputComponent = this.initInputComponent();

		this.getChildren().add(this.inputComponent);
	}

	/**
	 * Returns initialized input component.
	 */
	protected abstract UIInput initInputComponent();

	@Override
	public Collection<String> getEventNames() {
		return this.inputComponent.getEventNames();
	}

	@Override
	public void addClientBehavior(final String eventName, final ClientBehavior behavior) {
		this.inputComponent.addClientBehavior(eventName, behavior);
	}

	@Override
	public Map<String, List<ClientBehavior>> getClientBehaviors() {
		return this.inputComponent.getClientBehaviors();
	}

	@Override
	public String getDefaultEventName() {
		return this.inputComponent.getDefaultEventName();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getLabel() {
		return (String) this.getStateHelper().eval(PROPERTY_LABEL);
	}

	public void setLabel(final String label) {
		this.updateStateHelper(PROPERTY_LABEL, label);
	}

	public Boolean getReadonly() {
		return (Boolean) this.getStateHelper().eval(PROPERTY_READONLY, false);
	}

	public void setReadonly(final Boolean readonly) {
		this.updateStateHelper(PROPERTY_READONLY, readonly);
	}

	public Boolean getFloating() {
		return (Boolean) this.getStateHelper().eval(PROPERTY_FLOATING, false);
	}

	public void setFloating(final Boolean floating) {
		this.updateStateHelper(PROPERTY_FLOATING, floating);
	}

	public String getTooltip() {
		return (String) this.getStateHelper().eval(PROPERTY_TOOLTIP);
	}

	public void setTooltip(final String tooltip) {
		this.updateStateHelper(PROPERTY_TOOLTIP, tooltip);
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
