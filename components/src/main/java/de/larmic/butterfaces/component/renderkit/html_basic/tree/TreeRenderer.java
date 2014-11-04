package de.larmic.butterfaces.component.renderkit.html_basic.tree;

import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.HtmlBasicRenderer;
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

    private static final String DEFAULT_COLLAPSING_CLASS = "glyphicon glyphicon-plus-sign";
    private static final String DEFAULT_EXPANSION_CLASS = "glyphicon glyphicon-minus-sign";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTree htmlTree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", htmlTree);
        this.writeIdAttribute(context, writer, component);
        writer.writeAttribute("class", "butter-component-tree", null);

        this.encodeNode(htmlTree, writer, htmlTree.getValue(), !htmlTree.isHideRootNode());
    }

    private void encodeNode(final HtmlTree tree,
                            final ResponseWriter writer,
                            final Node node,
                            final boolean renderNode) throws IOException {
        if (renderNode) {
            writer.startElement("ul", tree);
            writer.startElement("li", tree);

            // collapse
            writer.startElement("span", tree);
            final String nodeClass = node.isLeaf() ? "butter-component-tree-leaf" : "butter-component-tree-node " + getCollapsingClass(tree);
            writer.writeAttribute("class", "butter-component-tree-jquery-marker " + nodeClass, null);
            writer.endElement("span");

            // icon
            writer.startElement("span", tree);
            writer.writeAttribute("class", "butter-component-tree-icon", null);
            writer.endElement("span");

            // title
            writer.startElement("span", tree);
            writer.writeAttribute("class", "butter-component-tree-title", null);
            writer.writeText(node.getTitle(), null);
            writer.endElement("span");
        }

        if (!node.isLeaf()) {
            final Collection<Node> subNodes = node.getSubNodes();
            for (Node subNode : subNodes) {
                this.encodeNode(tree, writer, subNode, true);
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

        final String pluginFunctionCall = "butterTree(" + createButterTreeJQueryParameter(htmlTree) + ")";
        RenderUtils.renderJQueryPluginCall(htmlTree.getClientId(), pluginFunctionCall, writer, component);

        writer.endElement("div");
    }

    private String createButterTreeJQueryParameter(final HtmlTree htmlTree) {
        final String expansionClass = getExpansionClass(htmlTree);
        final String collapsingClass = getCollapsingClass(htmlTree);

        return "{expansionClass: '" + expansionClass + "', collapsingClass: '" + collapsingClass + "'}";
    }

    private String getCollapsingClass(final HtmlTree htmlTree) {
        return StringUtils.isEmpty(htmlTree.getCollapsingClass())
                ? DEFAULT_COLLAPSING_CLASS : htmlTree.getCollapsingClass();
    }

    private String getExpansionClass(final HtmlTree htmlTree) {
        return StringUtils.isEmpty(htmlTree.getExpansionClass())
                ? DEFAULT_EXPANSION_CLASS : htmlTree.getExpansionClass();
    }
}
