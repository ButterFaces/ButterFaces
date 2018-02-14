package org.butterfaces.component.html.text;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.InputComponentFacet;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-number.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-01-baseClass.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-numberSpinner.jquery.js", target = "head")
})
@FacesComponent(HtmlNumber.COMPONENT_TYPE)
public class HtmlNumber extends HtmlInputText implements HtmlInputComponent, Tooltip, Label, Readonly {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.number";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.NumberRenderer";

    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_HTML5_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
    protected static final String PROPERTY_HTML5_MIN = "min";
    protected static final String PROPERTY_HTML5_MAX = "max";
    protected static final String PROPERTY_STEP = "step";
    protected static final String PROPERTY_VALIDATION_ERROR_PLACEMENT = "validationErrorPlacement";

    public HtmlNumber() {
        super();
        this.setRendererType(RENDERER_TYPE);

        addValidator(new Validator() {
            @Override
            public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
                try {
                    final long longValue = Long.parseLong(value.toString());

                    if (getMin() != null && !"".equals(getMin()) && longValue < Integer.valueOf(getMin())) {
                        throw new ValidatorException(new FacesMessage("Number is to small", String.format("%s is to small", value)));
                    }

                    if (getMax() != null && !"".equals(getMax()) && longValue > Integer.valueOf(getMax())) {
                        throw new ValidatorException(new FacesMessage("Number is to big", String.format("%s is to big", value)));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidatorException(new FacesMessage("No number", String.format("%s is no number", value)));
                }
            }
        });
    }

    @Override
    public List<InputComponentFacet> getSupportedFacets() {
        return Collections.emptyList();
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
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

    public String getStep() {
        return (String) this.getStateHelper().eval(PROPERTY_STEP);
    }

    public void setStep(final String step) {
        this.updateStateHelper(PROPERTY_STEP, step);
    }

    public boolean getAutoFocus() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS);
        return eval == null ? false : (Boolean) eval;
    }

    public void setAutoFocus(final boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
    }

    @Override
    public String getValidationErrorPlacement() {
        return (String) this.getStateHelper().eval(PROPERTY_VALIDATION_ERROR_PLACEMENT, "");
    }

    public void setValidationErrorPlacement(final String validationErrorPlacement) {
        this.updateStateHelper(PROPERTY_VALIDATION_ERROR_PLACEMENT, validationErrorPlacement);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
