/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.table.HtmlColumnNoMojarra;
import de.larmic.butterfaces.component.html.table.HtmlTableNoMojarra;
import de.larmic.butterfaces.util.StringUtils;

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
        final HtmlColumnNoMojarra column = (HtmlColumnNoMojarra) component;

        writer.startElement("td", component);
        writer.writeAttribute("class", "butter-component-table-column " + StringUtils.getNullSafeValue(column.getStyleClass()), "styleclass");
        if (StringUtils.isNotEmpty(column.getStyle())) {
            writer.writeAttribute("style", column.getStyle(), null);
        }

        if (component.getParent() instanceof HtmlTableNoMojarra && ((HtmlTableNoMojarra) component.getParent()).isHideColumn(column)) {
            writer.writeAttribute("style", "display:none", null);
        }
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
