package de.larmic.jsf2.component.html;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

@FacesComponent(HtmlTextV2.COMPONENT_TYPE)
public class HtmlTextV2 extends HtmlInputText {

	public static final String COMPONENT_TYPE = "de.larmic.component.textV2";
	public static final String COMPONENT_FAMILY = "de.larmic.component.family";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.BasicInputRenderer";

	protected static final String PROPERTY_FLOATING = "floating";
	protected static final String PROPERTY_TOOLTIP = "tooltip";
	protected static final String PROPERTY_READONLY = "readonly";

	public HtmlTextV2() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getTooltip() {
		return (String) this.getStateHelper().eval(PROPERTY_TOOLTIP);
	}

	public void setTooltip(final String tooltip) {
		this.updateStateHelper(PROPERTY_TOOLTIP, tooltip);
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

	private void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}

}
