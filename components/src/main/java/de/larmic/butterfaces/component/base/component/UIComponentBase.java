package de.larmic.butterfaces.component.base.component;

import javax.el.ValueExpression;

/**
 * Created by larmic on 15.04.15.
 */
public abstract class UIComponentBase extends javax.faces.component.UIComponentBase {

    protected void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }

}
