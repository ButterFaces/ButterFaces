package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-default.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-modal.js", target = "head")
})
@FacesComponent(HtmlModalPanel.COMPONENT_TYPE)
public class HtmlModalPanel extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.modal";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.ModalPanelRenderer";

	protected static final String PROPERTY_TITLE= "title";
	protected static final String PROPERTY_CANCEL_BTN_TEXT= "cancelButtonText";
	protected static final String PROPERTY_STYLE_CLASS = "styleClass";
	protected static final String PROPERTY_STYLE = "style";

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

	protected void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
