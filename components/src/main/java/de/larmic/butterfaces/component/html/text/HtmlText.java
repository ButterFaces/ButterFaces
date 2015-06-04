package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.html.feature.AutoFocus;
import de.larmic.butterfaces.component.html.feature.Label;
import de.larmic.butterfaces.component.html.feature.Placeholder;
import de.larmic.butterfaces.component.html.feature.Tooltip;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-tooltip.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.js", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlText.COMPONENT_TYPE)
public class HtmlText extends HtmlInputText implements HtmlInputComponent, AutoFocus, Placeholder, Tooltip, Label {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.text";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TextRenderer";

    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
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
	public List<InputComponentFacet> getSupportedFacets() {
		return Arrays.asList(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON,
				InputComponentFacet.BOOTSTRAP_INPUT_GROUP_RIGHT_ADDON,
				InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN,
				InputComponentFacet.BOOTSTRAP_INPUT_GROUP_RIGHT_BTN);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public Collection<String> getEventNames() {
		final ArrayList<String> eventNames = new ArrayList<>(super.getEventNames());
		eventNames.add("autocomplete");
		return eventNames;
	}

    @Override
    public boolean isHideLabel() {
		final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_LABEL);
		return eval == null ? false : (Boolean) eval;
    }

    public void setHideLabel(final boolean hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
    }

	@Override
	public String getPlaceholder() {
		return (String) this.getStateHelper().eval(PROPERTY_HTML5_PLACEHOLDER);
	}

	@Override
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

	@Override
    public boolean isAutoFocus() {
		final Object eval = this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS);
		return eval == null ? false : (Boolean) eval;
    }

	@Override
    public void setAutoFocus(final boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
    }

	protected void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
