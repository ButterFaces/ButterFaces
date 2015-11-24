package de.larmic.butterfaces.component.html.ajax;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
       @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-overlay.css", target = "head"),
        @ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-guid.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-overlay.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-waitingpanel.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-dots.jquery.js", target = "head")})
@FacesComponent(HtmlWaitingPanel.COMPONENT_TYPE)
public class HtmlWaitingPanel extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.waitingPanel";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.WaitingPanelRenderer";

    protected static final String PROPERTY_DELAY = "delay";
    protected static final String PROPERTY_BLOCKPAGE = "blockpage";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    public HtmlWaitingPanel() {
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

    public boolean isBlockpage() {
        final Object eval = this.getStateHelper().eval(PROPERTY_BLOCKPAGE);
        return eval == null ? true : (Boolean) eval;
    }

    public void setBlockpage(boolean blockpage) {
        this.updateStateHelper(PROPERTY_BLOCKPAGE, blockpage);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
