package de.larmic.butterfaces.component.renderkit.html_basic.tree;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
@FacesRenderer(componentFamily = HtmlTree.COMPONENT_FAMILY, rendererType = HtmlTree.RENDERER_TYPE)
public class TreeRenderer extends HtmlBasicRenderer {

    private static final String DEFAULT_COLLAPSING_CLASS = "glyphicon-chevron-up";
    private static final String DEFAULT_EXPANSION_CLASS = "glyphicon-chevron-down";

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

        // collapse
        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-tree-collapse glyphicon glyphicon-chevron-down", null);
        writer.endElement("span");

        // icon
        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-tree-icon", null);
        writer.endElement("span");

        // title
        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-tree-title", null);
        writer.writeText(node.getTitle(), null);
        writer.endElement("span");

        if (!node.isLeaf()) {
            final Collection<Node> subNodes = node.getSubNodes();
            for (Node subNode : subNodes) {
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

        // TODO [larmic] check client id not existing
        final String pluginFunctionCall = "butterTree(" + createButterTreeJQueryParameter(htmlTree) + ")";
        RenderUtils.renderJQueryPluginCall(htmlTree.getClientId(), pluginFunctionCall, writer, component);

        writer.endElement("div");
    }

    private String createButterTreeJQueryParameter(final HtmlTree htmlTree) {
        final String expansionClass = StringUtils.isEmpty(htmlTree.getExpansionClass())
                ? DEFAULT_EXPANSION_CLASS : htmlTree.getExpansionClass();
        final String collapsingClass = StringUtils.isEmpty(htmlTree.getCollapsingClass())
                ? DEFAULT_COLLAPSING_CLASS : htmlTree.getExpansionClass();

        return "{expansionClass: '" + expansionClass + "', collapsingClass: '" + collapsingClass + "'}";
    }
}
