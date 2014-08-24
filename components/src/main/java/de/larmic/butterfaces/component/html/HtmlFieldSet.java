package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({ @ResourceDependency(library = "css", name = "larmic-jsf2-components.css", target = "head"),
		@ResourceDependency(library = "js", name = "larmic-jsf2-components.js", target = "head") })
@FacesComponent(HtmlFieldSet.COMPONENT_TYPE)
public class HtmlFieldSet extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.fieldSet";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.FieldSetRenderer";

    protected static final String PROPERTY_LABEL = "label";

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

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
