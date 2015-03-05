package de.larmic.butterfaces.component.html.ajax;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-waiting.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-dots.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-waitingpanel.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-disableElements.jquery.js", target = "head") })
@FacesComponent(WaitingPanel.COMPONENT_TYPE)
public class WaitingPanel extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.waitingPanel";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.WaitingPanelRenderer";

    protected static final String PROPERTY_DELAY = "delay";
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

    public Integer getDelay() {
        return (Integer) this.getStateHelper().eval(PROPERTY_DELAY);
    }

    public void setDelay(final Integer delay) {
        this.updateStateHelper(PROPERTY_DELAY, delay);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
