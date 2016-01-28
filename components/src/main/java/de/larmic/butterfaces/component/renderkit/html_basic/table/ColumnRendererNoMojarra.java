/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.table.HtmlColumnNoMojarra;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlColumnNoMojarra.COMPONENT_FAMILY, rendererType = HtmlColumnNoMojarra.RENDERER_TYPE)
public class ColumnRendererNoMojarra extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", component);
        writer.writeAttribute("class", "butter-component-table-column", "styleclass");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeEnd(context, component);

        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("td");
    }
}
