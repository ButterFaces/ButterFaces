package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-default.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlText.COMPONENT_TYPE)
public class HtmlText extends HtmlInputText implements HtmlInputComponent {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.text";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TextRenderer";

	protected static final String PROPERTY_TOOLTIP = "tooltip";
    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
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
	public boolean supportInputGroupAddon() {
		return true;
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
    public boolean isHideLabel() {
		final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_LABEL);
		return eval == null ? false : (Boolean) eval;
    }

    public void setHideLabel(final boolean hideLabel) {
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
		final Object eval = this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS);
		return eval == null ? false : (Boolean) eval;
    }

    public void setAutoFocus(final boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
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

	private void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
