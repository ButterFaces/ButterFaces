package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.html.feature.AutoFocus;
import de.larmic.butterfaces.component.html.feature.Placeholder;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputSecret;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-default.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlSecret.COMPONENT_TYPE)
public class HtmlSecret extends HtmlInputSecret implements HtmlInputComponent, AutoFocus, Placeholder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.secret";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.SecretRenderer";

    protected static final String PROPERTY_TOOLTIP = "tooltip";
    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
    protected static final String PROPERTY_INPUT_STYLE_CLASS = "inputStyleClass";
    protected static final String PROPERTY_LABEL_STYLE_CLASS = "labelStyleClass";

    public HtmlSecret() {
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
    public String getTooltip() {
        return (String) this.getStateHelper().eval(PROPERTY_TOOLTIP);
    }

    public void setTooltip(final String tooltip) {
        this.updateStateHelper(PROPERTY_TOOLTIP, tooltip);
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

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
