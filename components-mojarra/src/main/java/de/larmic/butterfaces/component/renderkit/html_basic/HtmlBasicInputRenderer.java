package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.RequestStateManager;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by larmic on 12.09.15.
 */
public class HtmlBasicInputRenderer extends HtmlBasicRenderer {

    private static final Logger LOGGER = Logger.getLogger(HtmlBasicRenderer.class.getName());

    private boolean hasStringConverter = false;
    private boolean hasStringConverterSet = false;

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
            throws ConverterException {

        String newValue = (String) submittedValue;
        // if we have no local value, try to get the valueExpression.
        ValueExpression valueExpression = component.getValueExpression("value");
        Converter converter = null;

        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (null == converter && null != valueExpression) {
            Class converterType = valueExpression.getType(context.getELContext());
            // if converterType is null, assume the modelType is "String".
            if (converterType == null ||
                    converterType == Object.class) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                            "No conversion necessary for value {0} of component {1}",
                            new Object[]{
                                    submittedValue,
                                    component.getId()});
                }
                return newValue;
            }

            // If the converterType is a String, and we don't have a
            // converter-for-class for java.lang.String, assume the type is
            // "String".
            if (converterType == String.class && !hasStringConverter(context)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                            "No conversion necessary for value {0} of component {1}",
                            new Object[]{
                                    submittedValue,
                                    component.getId()});
                }
                return newValue;
            }
            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.

            try {
                Application application = context.getApplication();
                converter = application.createConverter(converterType);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                            "Created converter ({0}) for type {1} for component {2}.",
                            new Object[]{
                                    converter.getClass().getName(),
                                    converterType.getClass().getName(),
                                    component.getId()});
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                            "Could not instantiate converter for type {0}: {1}",
                            new Object[]{
                                    converterType,
                                    e.toString()});
                    LOGGER.log(Level.SEVERE, "", e);
                }
                return (null);
            }
        } else if (converter == null) {
            // if there is no valueExpression and converter attribute set,
            // assume the modelType as "String" since we have no way of
            // figuring out the type. So for the selectOne and
            // selectMany, converter has to be set if there is no
            // valueExpression attribute set on the component.
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        "No conversion necessary for value {0} of component {1}",
                        new Object[]{
                                submittedValue,
                                component.getId()});
                LOGGER.fine(" since there is no explicitly registered converter "
                        + "and the component value is not bound to a model property");
            }
            return newValue;
        }

        if (converter != null) {
            // If the conversion eventually falls to needing to use EL type coercion,
            // make sure our special ConverterPropertyEditor knows about this value.
            RequestStateManager.set(context,
                    RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                    component);
            return converter.getAsObject(context, component, newValue);
        } else {
            // throw converter exception.
            Object[] params = {
                    newValue,
                    "null Converter"
            };

            throw new ConverterException(MessageFactory.getMessage(
                    context, MessageUtils.CONVERSION_ERROR_MESSAGE_ID, params));
        }

    }

    private boolean hasStringConverter(FacesContext context) {
        if (!hasStringConverterSet) {
            hasStringConverter = (null !=
                    context.getApplication()
                            .createConverter(String.class));
            hasStringConverterSet = true;
        }
        return hasStringConverter;

    }

}
