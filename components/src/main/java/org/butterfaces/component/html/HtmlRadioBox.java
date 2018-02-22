package org.butterfaces.component.html;

import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-radiobox.js", target = "head")
})
@FacesComponent(HtmlRadioBox.COMPONENT_TYPE)
public class HtmlRadioBox extends HtmlInputText implements HtmlInputComponent, Tooltip, Label, Readonly {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.radioBox";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.RadioBox2Renderer";

    private static final String PROPERTY_VALUES = "values";
    private static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_VALIDATION_ERROR_PLACEMENT = "validationErrorPlacement";

    public HtmlRadioBox() {
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
    public boolean isHideLabel() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_LABEL);
        return eval == null ? false : (Boolean) eval;
    }

    public void setHideLabel(final boolean hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
    }

    public Object getValues() {
        return this.getStateHelper().eval(PROPERTY_VALUES);
    }

    public void setValues(Object values) {
        this.updateStateHelper(PROPERTY_VALUES, values);
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
