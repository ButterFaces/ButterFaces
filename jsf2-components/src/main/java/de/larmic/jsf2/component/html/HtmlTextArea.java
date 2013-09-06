package de.larmic.jsf2.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputTextarea;

@ResourceDependencies({ @ResourceDependency(library = "css", name = "larmic-jsf2-components.css", target = "head"),
		@ResourceDependency(library = "js", name = "larmic-jsf2-components.js", target = "head") })
@FacesComponent(HtmlTextArea.COMPONENT_TYPE)
public class HtmlTextArea extends HtmlInputTextarea implements HtmlInputComponent {

	public static final String COMPONENT_TYPE = "de.larmic.component.textArea";
	public static final String COMPONENT_FAMILY = "de.larmic.component.family";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.TextAreaRenderer";

	protected static final String PROPERTY_FLOATING = "floating";
	protected static final String PROPERTY_TOOLTIP = "tooltip";
	protected static final String PROPERTY_READONLY = "readonly";
	protected static final String PROPERTY_MAXLENGTH = "maxLength";
	protected static final String PROPERTY_PLACEHOLDER = "placeholder";

	public HtmlTextArea() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getTooltip() {
		return (String) this.getStateHelper().eval(PROPERTY_TOOLTIP);
	}

	public void setTooltip(final String tooltip) {
		this.updateStateHelper(PROPERTY_TOOLTIP, tooltip);
	}

	public String getPlaceholder() {
		return (String) this.getStateHelper().eval(PROPERTY_PLACEHOLDER);
	}

	public void setPlaceholder(final String placeholder) {
		this.updateStateHelper(PROPERTY_PLACEHOLDER, placeholder);
	}

	public Integer getMaxLength() {
		return (Integer) this.getStateHelper().eval(PROPERTY_MAXLENGTH);
	}

	public void setMaxLength(final Integer maxLength) {
		this.updateStateHelper(PROPERTY_MAXLENGTH, maxLength);
	}

	@Override
	public boolean getFloating() {
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
