package de.larmic.butterfaces.component.base.renderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by larmic on 03.11.14.
 */
public class HtmlBasicRenderer extends Renderer {

    private static final Logger LOGGER = Logger.getLogger(HtmlBasicRenderer.class.getName());

    public static final String ELEMENT_DIV = "div";
    public static final String ELEMENT_SPAN = "span";
    public static final String ELEMENT_SECTION = "section";

    public static final String ATTRIBUTE_STYLE = "style";
    public static final String ATTRIBUTE_CLASS = "class";

    protected void rendererParamsNotNull(final FacesContext context, final UIComponent component) {
        notNull("context", context);
        notNull("component", component);
    }

    private void notNull(String varname, Object var) {

        if (var == null) {
            throw new NullPointerException(varname);
        }

    }

    protected boolean shouldEncode(final UIComponent component) {
        return component.isRendered();
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
        writer.writeAttribute("id", clientId, "id");
        return clientId;
    }

    /**
     * @param component the component of interest
     * @return true if this renderer should render an id attribute.
     */
    protected boolean shouldWriteIdAttribute(UIComponent component) {

        // By default we only write the id attribute if:
        //
        // - We have a non-auto-generated id, or...
        // - We have client behaviors.
        //
        // We assume that if client behaviors are present, they
        // may need access to the id (AjaxBehavior certainly does).

        String id;
        return (null != (id = component.getId()) &&
                (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX) ||
                        ((component instanceof ClientBehaviorHolder) &&
                                !((ClientBehaviorHolder) component).getClientBehaviors().isEmpty())));
    }

    /**
     * <p>Render nested child components by invoking the encode methods
     * on those components, but only when the <code>rendered</code>
     * property is <code>true</code>.</p>
     *
     * @param context   FacesContext for the current request
     * @param component the component to recursively encode
     * @throws IOException if an error occurrs during the encode process
     */
    protected void encodeRecursive(FacesContext context, UIComponent component)
            throws IOException {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        // Render this component and its children recursively
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator<UIComponent> kids = getChildren(component);
            while (kids.hasNext()) {
                UIComponent kid = kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);

    }

    /**
     * @param component <code>UIComponent</code> for which to extract children
     * @return an Iterator over the children of the specified
     * component, selecting only those that have a
     * <code>rendered</code> property of <code>true</code>.
     */
    protected Iterator<UIComponent> getChildren(UIComponent component) {

        int childCount = component.getChildCount();
        if (childCount > 0) {
            return component.getChildren().iterator();
        } else {
            return Collections.<UIComponent>emptyList().iterator();
        }

    }

    /**
     * @param component Component from which to return a facet
     * @param name      Name of the desired facet
     * @return the specified facet from the specified component, but
     * <strong>only</strong> if its <code>rendered</code> property is
     * set to <code>true</code>.
     */
    protected UIComponent getFacet(UIComponent component, String name) {

        UIComponent facet = null;
        if (component.getFacetCount() > 0) {
            facet = component.getFacet(name);
            if ((facet != null) && !facet.isRendered()) {
                facet = null;
            }
        }
        return (facet);

    }
}
