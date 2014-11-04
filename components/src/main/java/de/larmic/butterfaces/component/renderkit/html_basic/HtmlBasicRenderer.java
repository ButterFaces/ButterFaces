package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Created by larmic on 03.11.14.
 */
public class HtmlBasicRenderer extends Renderer {

    public static final String ELEMENT_DIV = "div";
    public static final String ELEMENT_SPAN = "span";

    protected void rendererParamsNotNull(final FacesContext context, final UIComponent component) {
        Util.notNull("context", context);
        Util.notNull("component", component);
    }

    protected boolean shouldEncode(final UIComponent component) {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
//            if (logger.isLoggable(Level.FINE)) {
//                logger.log(Level.FINE,
//                        "End encoding component {0} since rendered attribute is set to false",
//                        component.getId());
//            }
            return false;
        }
        return true;

    }

    protected String writeIdAttributeIfNecessary(final FacesContext context,
                                                 final ResponseWriter writer,
                                                 final UIComponent component) {
        String id = null;
        if (shouldWriteIdAttribute(component)) {
            this.writeIdAttribute(context, writer, component);
        }
        return id;
    }

    protected String writeIdAttribute(final FacesContext context,
                                      final ResponseWriter writer,
                                      final UIComponent component) {
        String id = null;
        try {
            writer.writeAttribute("id", id = component.getClientId(context), "id");
        } catch (IOException e) {
//                if (logger.isLoggable(Level.WARNING)) {
//                    String message = MessageUtils.getExceptionMessageString
//                            (MessageUtils.CANT_WRITE_ID_ATTRIBUTE_ERROR_MESSAGE_ID,
//                                    e.getMessage());
//                    logger.warning(message);
//                }
        }

        return id;
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
}
