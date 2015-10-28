package de.larmic.butterfaces.resolver;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ELResolver {

    public static Object resolve(final FacesContext facesContext,
                                 final UIComponent component,
                                 final String expressionString,
                                 final String valueExpressionString,
                                 final Object value) {
        final ValueExpression valueExpression = component.getValueExpression(expressionString);
        final ELContext elContext = facesContext.getELContext();

        Object valueFromValueExpression = null;

        if (valueExpression != null) {
            setValue2ValueExpression(value, "#{" + valueExpressionString + "}");
            return valueExpression.getValue(elContext);
        }

        return valueFromValueExpression;
    }

    private static void setValue2ValueExpression(final Object value, final String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();

        ValueExpression targetExpression =
                facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, expression, Object.class);
        targetExpression.setValue(elContext, value);
    }
}
