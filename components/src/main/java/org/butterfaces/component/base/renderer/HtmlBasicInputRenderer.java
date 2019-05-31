/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2019.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.base.renderer;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lars Michaelis
 */
public class HtmlBasicInputRenderer extends HtmlBasicRenderer {

    private static final Logger LOGGER = Logger.getLogger(HtmlBasicRenderer.class.getName());

    private boolean hasStringConverter = false;
    private boolean hasStringConverterSet = false;

    @Override
    public Object getConvertedValue(final FacesContext context,
                                    final UIComponent component,
                                    final Object submittedValue) throws ConverterException {

        final String newValue = (String) submittedValue;
        // if we have no local value, try to get the valueExpression.
        final ValueExpression valueExpression = component.getValueExpression("value");
        Converter converter = this.getValueHolderConverter(component);

        if (null == converter && null != valueExpression) {
            final Class converterType = valueExpression.getType(context.getELContext());

            if (this.shouldNotBeConverted(converterType, context)) {
                this.logFine("No conversion necessary for value {0} of component {1}", submittedValue, component.getId());
                return newValue;
            }

            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
            try {
                converter = context.getApplication().createConverter(converterType);
                this.logFine("Created converter ({0}) for type {1} for component {2}.",
                    converter.getClass().getName(),
                    converterType.getName(),
                    component.getId());
            } catch (Exception e) {
                this.log(Level.SEVERE, "Could not instantiate converter for type {0}: {1}", e, converterType, e.toString());
                return null;
            }
        } else if (converter == null) {
            // if there is no valueExpression and converter attribute set,
            // assume the modelType as "String" since we have no way of
            // figuring out the type. So for the selectOne and
            // selectMany, converter has to be set if there is no
            // valueExpression attribute set on the component.
            final String message = "No conversion necessary for value {0} of component {1} "
                + "since there is no explicitly registered converter "
                + "and the component value is not bound to a model property";
            this.logFine(message, submittedValue, component.getId());
            return newValue;
        }

        return converter.getAsObject(context, component, newValue);
    }

    /**
     * If converterType is null, assume the modelType is "String".
     * If the converterType is a String, and we don't have a converter-for-class for java.lang.String, assume the type is "String".
     */
    private boolean shouldNotBeConverted(final Class converterType, final FacesContext context) {
        return converterType == null
            || converterType == Object.class
            || (converterType == String.class && hasNoStringConverter(context));
    }

    private void logFine(final String message, final Object... parameters) {
        this.log(Level.FINE, message, null, parameters);
    }

    private void log(final Level level, final String message, final Throwable throwable, final Object... parameters) {
        if (LOGGER.isLoggable(level)) {
            LOGGER.log(level, message, parameters);
            if (throwable != null) {
                LOGGER.log(level, "", throwable);
            }
        }
    }

    private Converter getValueHolderConverter(final UIComponent component) {
        // If there is a converter attribute, use it to to ask application instance for a converter with this identifier.
        if (component instanceof ValueHolder) {
            return ((ValueHolder) component).getConverter();
        }
        return null;
    }

    private boolean hasNoStringConverter(final FacesContext context) {
        if (!hasStringConverterSet) {
            hasStringConverter = context.getApplication().createConverter(String.class) != null;
            hasStringConverterSet = true;
        }

        return !hasStringConverter;
    }

}
