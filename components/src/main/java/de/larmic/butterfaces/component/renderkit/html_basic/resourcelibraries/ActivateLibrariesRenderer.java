package de.larmic.butterfaces.component.renderkit.html_basic.resourcelibraries;

import de.larmic.butterfaces.component.html.resourcelibraries.HtmlActivateLibraries;
import de.larmic.butterfaces.component.base.renderer.HtmlDeprecatedBasicRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 17.09.14.
 */
@FacesRenderer(componentFamily = HtmlActivateLibraries.COMPONENT_FAMILY, rendererType = HtmlActivateLibraries.RENDERER_TYPE)
public class ActivateLibrariesRenderer extends HtmlDeprecatedBasicRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        context.getResponseWriter().writeComment("ButterFaces information:\ntag l<:activateLibraries /> is used to enable Bootstrap and jQuery!");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }
    }
}
