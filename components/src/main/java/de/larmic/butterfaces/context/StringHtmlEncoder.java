/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.context;

import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Util class to use {@link FacesContext}s {@link javax.faces.context.ResponseWriter} to create a html string.
 *
 * @author Lars Michaelis
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
        return encodeComponentWithSurroundingDiv(context, component, null);
    }

    /**
     * Encodes complete component by calling {@link UIComponent#encodeAll(FacesContext)}.
     *
     * @param component  component
     * @param context    {@link FacesContext}
     * @param styleClass a div wrapper style class
     * @return the rendered string.
     * @throws IOException thrown by writer
     */
    public static String encodeComponentWithSurroundingDiv(final FacesContext context,
                                                           final UIComponent component,
                                                           final String styleClass) throws IOException {
        if (StringUtils.isNotEmpty(styleClass)) {
            return "<div class=\"" + styleClass + "\">" + encodeComponent(context, component) + "</div>";
        }
        return "<div>" + encodeComponent(context, component) + "</div>";
    }

    /**
     * Encodes complete component by calling {@link UIComponent#encodeAll(FacesContext)}. Surrounds template with given
     * styleclass if template does not contains it.
     *
     * @param component  component
     * @param context    {@link FacesContext}
     * @param styleClass a div wrapper style class
     * @return the rendered string.
     * @throws IOException thrown by writer
     */
    public static String encodeComponentWithSurroundingDivIfNecessary(final FacesContext context,
                                                                      final UIComponent component,
                                                                      final String styleClass) throws IOException {
        final String encodedFacet = encodeComponent(context, component);
        if (StringUtils.isNotEmpty(styleClass) && !encodedFacet.contains(styleClass)) {
            return "<div class=\"" + styleClass + "\">" + encodedFacet + "</div>";
        }
        return "<div>" + encodedFacet + "</div>";
    }
}
