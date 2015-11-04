package de.larmic.butterfaces.context;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Util class to use {@link FacesContext}s {@link javax.faces.context.ResponseWriter} to create a html string.
 */
public class StringHtmlEncoder {

    /**
     * Encodes complete component by calling {@link UIComponent#encodeAll(FacesContext)}.
     *
     * @param component component
     * @param context   {@link FacesContext}
     * @return the rendered string.
     * @throws IOException thrown by writer
     */
    public static String encodeComponent(final FacesContext context,
                                         final UIComponent component) throws IOException {
        final FacesContextStringResolverWrapper resolver = new FacesContextStringResolverWrapper(context);
        component.encodeAll(resolver);
        return resolver.getStringWriter().toString().replace("\n", "").trim();
    }

    /**
     * Encodes complete component by calling {@link UIComponent#encodeAll(FacesContext)}.
     *
     * @param component component
     * @param context   {@link FacesContext}
     * @return the rendered string.
     * @throws IOException thrown by writer
     */
    public static String encodeComponentWithSurroundingDiv(final FacesContext context,
                                                           final UIComponent component) throws IOException {
        return "<div>" + encodeComponent(context, component) + "</div>";
    }
}
