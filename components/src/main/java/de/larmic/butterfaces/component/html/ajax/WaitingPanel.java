package de.larmic.butterfaces.component.html.ajax;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-waitingpanel.jquery.js", target = "head") })
@FacesComponent(WaitingPanel.COMPONENT_TYPE)
public class WaitingPanel extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.waitingPanel";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.WaitingPanelRenderer";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

	public WaitingPanel() {
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

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
