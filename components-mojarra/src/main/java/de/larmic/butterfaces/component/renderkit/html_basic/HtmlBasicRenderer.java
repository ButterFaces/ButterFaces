package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by larmic on 20.08.15.
 */
public class HtmlBasicRenderer extends com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer {

    /**
     * Render boolean value if attribute is set to true.
     */
    protected void renderBooleanValue(final UIComponent component,
                                      final ResponseWriter writer,
                                      final String attributeName) throws IOException {
        if (component.getAttributes().get(attributeName) != null && Boolean.valueOf(component.getAttributes().get(attributeName).toString())) {
            writer.writeAttribute(attributeName, true, attributeName);
        }
    }

    /**
     * Render string value if attribute is not empty.
     */
    protected void renderStringValue(final UIComponent component,
                                     final ResponseWriter writer,
                                     final String attributeName) throws IOException {
        if (component.getAttributes().get(attributeName) != null
                && StringUtils.isNotEmpty(component.getAttributes().get(attributeName).toString())
                && shouldRenderAttribute(component.getAttributes().get(attributeName))) {
            writer.writeAttribute(attributeName, component.getAttributes().get(attributeName), attributeName);
        }
    }

    /**
     * Render string value if attribute is equals to matching value.
     */
    protected void renderStringValue(final UIComponent component,
                                     final ResponseWriter writer,
                                     final String attributeName,
                                     final String matchingValue) throws IOException {
        if (component.getAttributes().get(attributeName) != null
                && matchingValue.equalsIgnoreCase(component.getAttributes().get(attributeName).toString())
                && shouldRenderAttribute(component.getAttributes().get(attributeName))) {
            writer.writeAttribute(attributeName, matchingValue, attributeName);
        }
    }

    protected void renderEventValue(final UIComponent component,
                                    final ResponseWriter writer,
                                    final String attributeName,
                                    final String eventName) throws IOException {
        MojarraRenderUtils.renderPassThruAttributes(writer, component, attributeName, eventName);
    }

    protected boolean shouldRenderAttribute(Object value) {
        if (value == null)
            return false;

        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            final Number number = (Number) value;

            if (value instanceof Integer)
                return number.intValue() != Integer.MIN_VALUE;
            else if (value instanceof Double)
                return number.doubleValue() != Double.MIN_VALUE;
            else if (value instanceof Long)
                return number.longValue() != Long.MIN_VALUE;
            else if (value instanceof Byte)
                return number.byteValue() != Byte.MIN_VALUE;
            else if (value instanceof Float)
                return number.floatValue() != Float.MIN_VALUE;
            else if (value instanceof Short)
                return number.shortValue() != Short.MIN_VALUE;
        }

        return true;
    }

    protected void encodeRecursive(final FacesContext context,
                                   final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        component.encodeBegin(context);

        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            final Iterator<UIComponent> childComponentsIterator = getChildren(component);
            while (childComponentsIterator.hasNext()) {
                encodeRecursive(context, childComponentsIterator.next());
            }
        }

        component.encodeEnd(context);
    }

    @Override
    public void encodeChildren(final FacesContext context,
                               final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                encodeRecursive(context, child);
            }
        }
    }
}
