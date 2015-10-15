package de.larmic.butterfaces.component.base.renderer;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Basic butterfaces renderer.
 */
public class HtmlBasicRenderer extends Renderer {

    public static final String ELEMENT_DIV = "div";
    public static final String ELEMENT_SPAN = "span";
    public static final String ELEMENT_SECTION = "section";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_STYLE = "style";
    public static final String ATTRIBUTE_CLASS = "class";
    public static final String ATTRIBUTE_PLACEHOLDER = "placeholder";

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

    @Override
    public void encodeEnd(final FacesContext context,
                          final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        getEndTextToRender(context, component, getCurrentValue(context, component));
    }

    /**
     * @param component Component from which to return a facet
     * @param name      Name of the desired facet
     * @return the specified facet from the specified component, but
     * <strong>only</strong> if its <code>rendered</code> property is
     * set to <code>true</code>.
     */
    protected UIComponent getFacet(UIComponent component, String name) {
        final UIComponent facet = component.getFacet(name);
        return facet != null && facet.isRendered() ? facet : null;
    }

    @Override
    public void decode(final FacesContext context,
                       final UIComponent component) {
        if (!component.isRendered()) {
            return;
        }

        if (!(component instanceof UIInput)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        final Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        final String newValue = requestMap.get(clientId);
        if (newValue != null) {
            setSubmittedValue(component, newValue);
        }
    }

    protected final String decodeBehaviors(final FacesContext context,
                                           final UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder)) {
            return null;
        }

        final ClientBehaviorHolder holder = (ClientBehaviorHolder) component;
        final Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return null;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null) {
            final List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if (behaviorsForEvent != null && behaviorsForEvent.size() > 0) {
                final String behaviorSource = params.get("javax.faces.source");
                final String clientId = component.getClientId();
                if (isBehaviorSource(behaviorSource, clientId)) {
                    for (ClientBehavior behavior : behaviorsForEvent) {
                        behavior.decode(context, component);
                    }
                }

                return clientId;
            }
        }

        return null;
    }

    protected boolean isBehaviorSource(final String behaviorSourceId,
                                       final String componentClientId) {
        return (behaviorSourceId != null && behaviorSourceId.equals(componentClientId));
    }

    protected void setSubmittedValue(final UIComponent component,
                                     final Object value) {
        if (component instanceof UIInput) {
            ((UIInput) component).setSubmittedValue(value);
        }
    }

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

    protected String getCurrentValue(FacesContext context, final UIComponent component) {

        if (component instanceof UIInput) {
            Object submittedValue = ((UIInput) component).getSubmittedValue();
            if (submittedValue != null) {
                // value may not be a String...
                return submittedValue.toString();
            }
        }

        if (component instanceof ValueHolder) {
            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();
            final Converter converter = valueHolder.getConverter();

            if (converter != null && value != null) {
                return converter.getAsString(context, component, value);
            }

            if (value != null) {
                return value.toString();
            }
        }

        return null;

    }

    protected void getEndTextToRender(final FacesContext context,
                                      final UIComponent component,
                                      final String currentValue) throws IOException {
        // no-op unless overridden
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

    protected String writeIdAttributeIfNecessary(final FacesContext context,
                                                 final ResponseWriter writer,
                                                 final UIComponent component) throws IOException {
        if (shouldWriteIdAttribute(component)) {
            return this.writeIdAttribute(context, writer, component);
        }
        return null;
    }

    protected String writeIdAttribute(final FacesContext context,
                                      final ResponseWriter writer,
                                      final UIComponent component) throws IOException {
        final String clientId = component.getClientId(context);
        writer.writeAttribute(ATTRIBUTE_ID, clientId, ATTRIBUTE_ID);
        return clientId;
    }

    protected boolean shouldWriteIdAttribute(UIComponent component) {
        String id;
        return (null != (id = component.getId()) &&
                (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX) ||
                        ((component instanceof ClientBehaviorHolder) &&
                                !((ClientBehaviorHolder) component).getClientBehaviors().isEmpty())));
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

    protected Iterator<UIComponent> getChildren(UIComponent component) {
        if (component.getChildCount() > 0) {
            return component.getChildren().iterator();
        } else {
            return Collections.<UIComponent>emptyList().iterator();
        }

    }
}
