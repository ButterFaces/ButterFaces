package org.butterfaces.component.renderkit.html_basic.resourcelibraries;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.resourcelibraries.HtmlActivateLibraries;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 17.09.14.
 */
@FacesRenderer(componentFamily = HtmlActivateLibraries.COMPONENT_FAMILY, rendererType = HtmlActivateLibraries.RENDERER_TYPE)
public class ActivateLibrariesRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        context.getResponseWriter().writeComment("ButterFaces information:\ntag l<:activateLibraries /> is used to enable Bootstrap and jQuery!");
    }
}
