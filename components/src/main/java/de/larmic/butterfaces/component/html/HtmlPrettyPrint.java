package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-prettyprint.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-prettyprint.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "prettify.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "prettify.js", target = "head"),})
@FacesComponent(HtmlPrettyPrint.COMPONENT_TYPE)
public class HtmlPrettyPrint extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.prettyprint";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.PrettyPrintRenderer";

    protected static final String PROPERTY_LANGUAGE = "language";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    public HtmlPrettyPrint() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLanguage() {
        return (String) this.getStateHelper().eval(PROPERTY_LANGUAGE);
    }

    public void setLanguage(final String language) {
        this.updateStateHelper(PROPERTY_LANGUAGE, language);
    }

    public String getStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setStyleClass(final String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE);
    }

    public void setStyle(final String style) {
        this.updateStateHelper(PROPERTY_STYLE, style);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
