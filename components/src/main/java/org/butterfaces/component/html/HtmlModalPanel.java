package org.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-modal.js", target = "head")
})
@FacesComponent(HtmlModalPanel.COMPONENT_TYPE)
public class HtmlModalPanel extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.modal";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.ModalPanelRenderer";

    protected static final String PROPERTY_TITLE = "title";
    protected static final String PROPERTY_CANCEL_BTN_TEXT = "cancelButtonText";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";
    protected static final String PROPERTY_ON_SHOW = "onShow";
    protected static final String PROPERTY_ON_SHOWN = "onShown";
    protected static final String PROPERTY_ON_HIDE = "onHide";
    protected static final String PROPERTY_ON_HIDDEN = "onHidden";

    public HtmlModalPanel() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTitle() {
        return (String) this.getStateHelper().eval(PROPERTY_TITLE);
    }

    public void setTitle(final String title) {
        this.updateStateHelper(PROPERTY_TITLE, title);
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

    public String getCancelButtonText() {
        return (String) this.getStateHelper().eval(PROPERTY_CANCEL_BTN_TEXT);
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.updateStateHelper(PROPERTY_CANCEL_BTN_TEXT, cancelButtonText);
    }

    public String getOnShow() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_SHOW);
    }

    public void setOnShow(String onShow) {
        this.updateStateHelper(PROPERTY_ON_SHOW, onShow);
    }

    public String getOnShown() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_SHOWN);
    }

    public void setOnShown(String onShown) {
        this.updateStateHelper(PROPERTY_ON_SHOWN, onShown);
    }

    public String getOnHide() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_HIDE);
    }

    public void setOnHide(String onHide) {
        this.updateStateHelper(PROPERTY_ON_HIDE, onHide);
    }

    public String getOnHidden() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_HIDDEN);
    }

    public void setOnHidden(String onHidden) {
        this.updateStateHelper(PROPERTY_ON_HIDDEN, onHidden);
    }

    protected void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
