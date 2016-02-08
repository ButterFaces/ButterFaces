/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@FacesComponent(HtmlColumnNoMojarra.COMPONENT_TYPE)
public class HtmlColumnNoMojarra extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.columnNoMojarra";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.ColumnRendererNoMojarra";

    protected static final String PROPERTY_LABEL = "label";

    public HtmlColumnNoMojarra() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLabel() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL);
    }

    public void setLabel(final String label) {
        this.updateStateHelper(PROPERTY_LABEL, label);
    }

    protected void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
