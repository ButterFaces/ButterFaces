package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces.css", target = "head"),
		@ResourceDependency(library = "js", name = "butterfaces.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.3.0.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.3.0.min.js", target = "head") })
@FacesComponent(HtmlFieldSet.COMPONENT_TYPE)
public class HtmlFieldSet extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.fieldSet";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.FieldSetRenderer";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_BADGE_TEXT = "badgeText";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

	public HtmlFieldSet() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

    public String getLabel() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL);
    }

    public void setLabel(final String title) {
        this.updateStateHelper(PROPERTY_LABEL, title);
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

    public String getBadgeText() {
        return (String) this.getStateHelper().eval(PROPERTY_BADGE_TEXT);
    }

    public void setBadgeText(final String badgeText) {
        this.updateStateHelper(PROPERTY_BADGE_TEXT, badgeText);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
