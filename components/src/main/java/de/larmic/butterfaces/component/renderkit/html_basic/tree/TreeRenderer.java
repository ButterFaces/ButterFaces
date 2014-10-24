package de.larmic.butterfaces.component.renderkit.html_basic.tree;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 24.10.14.
 */
@FacesRenderer(componentFamily = HtmlTree.COMPONENT_FAMILY, rendererType = HtmlTree.RENDERER_TYPE)
public class TreeRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTree htmlTree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", htmlTree);
        this.writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("class", "butter-component-tree", null);

        this.encodeNode(htmlTree, writer, htmlTree.getValue());
    }

    private void encodeNode(final HtmlTree component, final ResponseWriter writer, final Node node) throws IOException {
        writer.startElement("ul", component);
        writer.startElement("li", component);
        writer.startElement("span", component);
        writer.writeText(node.getTitle(), null);
        writer.endElement("span");

        if (!node.isLeaf()) {
            for (Node subNode : node.getSubNodes()) {
                this.encodeNode(component, writer, subNode);
            }
        }

        writer.endElement("li");
        writer.endElement("ul");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTree htmlTree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");
    }
}
