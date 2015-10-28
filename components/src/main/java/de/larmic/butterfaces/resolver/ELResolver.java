package de.larmic.butterfaces.resolver;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ELResolver {

    public static Object resolveValueFromValueExpression(final FacesContext context,
                                                         final UIComponent component,
                                                         final String expressionAttribute,
                                                         final String expressionValue,
                                                         final Object value) {
        final ValueExpression valueExpression = component.getValueExpression(expressionAttribute);
        final ELContext elContext = context.getELContext();

        if (valueExpression != null) {
            setValue2ValueExpression(value, "#{" + expressionValue + "}", context);
            return valueExpression.getValue(elContext);
        }

        return null;
    }

    public static void setValue2ValueExpression(final Object value, final String expression, final FacesContext facesContext) {
        final ELContext elContext = facesContext.getELContext();
        final ValueExpression valueExpression =
                facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, expression, Object.class);
        valueExpression.setValue(elContext, value);
    }
}
