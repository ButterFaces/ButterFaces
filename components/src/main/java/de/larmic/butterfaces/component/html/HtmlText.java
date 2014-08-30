package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces.css", target = "head"),
		@ResourceDependency(library = "js", name = "butterfaces.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-reduced-3.2.0.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-1.11.1.min.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlText.COMPONENT_TYPE)
public class HtmlText extends HtmlInputText implements HtmlInputComponent {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.text";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TextRenderer";

	protected static final String PROPERTY_FLOATING = "floating";
	protected static final String PROPERTY_TOOLTIP = "tooltip";
    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
	protected static final String PROPERTY_COMPONENT_STYLE_CLASS = "componentStyleClass";
    protected static final String PROPERTY_INPUT_STYLE_CLASS = "inputStyleClass";
	protected static final String PROPERTY_LABEL_STYLE_CLASS = "labelStyleClass";
	protected static final String PROPERTY_HTML5_PLACEHOLDER = "placeholder";
	protected static final String PROPERTY_HTML5_TYPE = "type";
	protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
	protected static final String PROPERTY_HTML5_PATTERN = "pattern";
	protected static final String PROPERTY_HTML5_MIN = "min";
	protected static final String PROPERTY_HTML5_MAX = "max";

	public HtmlText() {
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

    @Override
    public boolean getHideLabel() {
        return (Boolean) this.getStateHelper().eval(PROPERTY_HIDE_LABEL, false);
    }

    public void setHideLabel(final String hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
    }

	public String getPlaceholder() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_PLACEHOLDER);
	}

	public void setPlaceholder(final String placeholder) {
		this.updateStateHelper(PROPERTY_HTML5_PLACEHOLDER, placeholder);
	}

    public String getType() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_TYPE);
	}

	public void setType(final String type) {
		this.updateStateHelper(PROPERTY_HTML5_TYPE, type);
	}

    public String getPattern() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_PATTERN);
	}

	public void setPattern(final String pattern) {
		this.updateStateHelper(PROPERTY_HTML5_PATTERN, pattern);
	}

    public String getMin() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_MIN);
	}

	public void setMin(final String min) {
		this.updateStateHelper(PROPERTY_HTML5_MIN, min);
	}

    public String getMax() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_MAX);
	}

	public void setMax(final String max) {
		this.updateStateHelper(PROPERTY_HTML5_MAX, max);
	}

    public boolean getAutoFocus() {
        return (Boolean) this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS, false);
    }

    public void setAutoFocus(final Boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
    }

    @Override
    public String getComponentStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_COMPONENT_STYLE_CLASS);
    }

    public void setComponentStyleClass(final String componentStyleClass) {
        this.updateStateHelper(PROPERTY_COMPONENT_STYLE_CLASS, componentStyleClass);
    }

    @Override
    public String getInputStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_INPUT_STYLE_CLASS);
    }

    public void setInputStyleClass(final String inputStyleClass) {
        this.updateStateHelper(PROPERTY_INPUT_STYLE_CLASS, inputStyleClass);
    }

    @Override
    public String getLabelStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL_STYLE_CLASS);
    }

    public void setLabelStyleClass(final String labelStyleClass) {
        this.updateStateHelper(PROPERTY_LABEL_STYLE_CLASS, labelStyleClass);
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
