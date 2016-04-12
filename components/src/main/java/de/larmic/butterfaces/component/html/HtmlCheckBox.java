package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.html.feature.AutoFocus;
import de.larmic.butterfaces.component.html.feature.Label;
import de.larmic.butterfaces.component.html.feature.Readonly;
import de.larmic.butterfaces.component.html.feature.Tooltip;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head")
})
@FacesComponent(HtmlCheckBox.COMPONENT_TYPE)
public class HtmlCheckBox extends HtmlSelectBooleanCheckbox implements HtmlInputComponent, AutoFocus, Tooltip, Label, Readonly {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.checkBox";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.CheckBoxRenderer";

    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_DESCRIPTION = "description";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";

    public HtmlCheckBox() {
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

    public String getDescription() {
        return (String) this.getStateHelper().eval(PROPERTY_DESCRIPTION);
    }

    public void setDescription(final String description) {
        this.updateStateHelper(PROPERTY_DESCRIPTION, description);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
