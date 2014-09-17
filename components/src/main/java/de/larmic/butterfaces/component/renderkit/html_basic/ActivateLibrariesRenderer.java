package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlActivateLibraries;

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
        rendererParamsNotNull(context, component);

        /*
        final ResponseWriter writer = context.getResponseWriter();

        writer.writeText("\n<!--\n", null);
        writer.writeText("   ButterFaces information:\n", null);
        writer.writeText("   tag <l:activateLibraries /> is used to enable Bootstrap and jQuery!\n", null);
        writer.writeText("-->;\n", null);*/
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);
    }
}
