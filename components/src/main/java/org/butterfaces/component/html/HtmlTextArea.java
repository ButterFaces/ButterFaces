package org.butterfaces.component.html;

import java.util.Collections;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;

import org.butterfaces.component.html.feature.AutoFocus;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.MaxLength;
import org.butterfaces.component.html.feature.Placeholder;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.resolver.WebXmlParameters;
import org.butterfaces.component.html.feature.*;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-util-string.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-util-object.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-maxlength.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-multiline-placeholder.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-expandable.jquery.js", target = "head")
})
@FacesComponent(HtmlTextArea.COMPONENT_TYPE)
public class HtmlTextArea extends HtmlInputTextarea implements HtmlInputComponent, Placeholder, AutoFocus, Tooltip, Label, Readonly, MaxLength {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.textArea";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.TextAreaRenderer";

    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_MAXLENGTH = "maxLength";
    protected static final String PROPERTY_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_EXPANDABLE = "expandable";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
    protected static final String PROPERTY_VALIDATION_ERROR_PLACEMENT = "validationErrorPlacement";

    public HtmlTextArea() {
        super();
        this.setRendererType(RENDERER_TYPE);
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
    public void setValue(Object value) {
        if (value instanceof String && new WebXmlParameters(FacesContext.getCurrentInstance().getExternalContext()).isAutoTrimInputFields()) {
            super.setValue(((String) value).trim());
        } else {
            super.setValue(value);
        }
    }

    @Override
    public Object getSubmittedValue() {
        final Object submittedValue = super.getSubmittedValue();

        if (submittedValue instanceof String && new WebXmlParameters(FacesContext.getCurrentInstance().getExternalContext()).isAutoTrimInputFields()) {
            return ((String) submittedValue).trim();
        } else {
            return submittedValue;
        }
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

    @Override
    public boolean isHideLabel() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_LABEL);
        return eval == null ? false : (Boolean) eval;
    }

    public void setHideLabel(final boolean hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
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

    public Boolean getExpandable() {
        return (Boolean) this.getStateHelper().eval(PROPERTY_EXPANDABLE);
    }

    public void setExpandable(final Boolean expandable) {
        this.updateStateHelper(PROPERTY_EXPANDABLE, expandable);
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
