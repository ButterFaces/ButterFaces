package de.larmic.butterfaces.component.renderkit.html_basic.tree;

import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 24.10.14.
 */
@FacesRenderer(componentFamily = HtmlTree.COMPONENT_FAMILY, rendererType = HtmlTree.RENDERER_TYPE)
public class TreeRenderer extends HtmlBasicRenderer {

    private static final String DEFAULT_COLLAPSING_CLASS = "glyphicon glyphicon-minus-sign";
    private static final String DEFAULT_EXPANSION_CLASS = "glyphicon glyphicon-plus-sign";

    private final Map<String, Node> nodes = new HashMap<>();
    private Node selectedNode = null;
    private boolean nodeIconsFound = false;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTree htmlTree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement(ELEMENT_DIV, htmlTree);
        this.writeIdAttribute(context, writer, component);
        writer.writeAttribute("class", "butter-component-tree", null);

        final ClientBehaviorContext behaviorContext =
                ClientBehaviorContext.createClientBehaviorContext(context,
                        htmlTree, "click", component.getClientId(context), null);

        this.encodeNode(htmlTree, writer, behaviorContext, htmlTree.getValue(), 0, 0, !htmlTree.isHideRootNode());
    }

    private void encodeNode(final HtmlTree tree,
                            final ResponseWriter writer,
                            final ClientBehaviorContext behaviorContext,
                            final Node node,
                            final int depth,
                            final int childNumber,
                            final boolean renderNode) throws IOException {
        if (renderNode) {
            writer.startElement("ul", tree);
            writer.startElement("li", tree);

            writer.startElement(ELEMENT_DIV, tree);
            writer.writeAttribute("class", "butter-component-tree-row", null);

            // collapse
            writer.startElement(ELEMENT_SPAN, tree);
            final String nodeClass = node.isLeaf() ? "butter-component-tree-leaf" : "butter-component-tree-node " + getCollapsingClass(tree);
            writer.writeAttribute("class", "butter-component-tree-jquery-marker " + nodeClass, null);
            writer.endElement(ELEMENT_SPAN);

            // icon
            writer.startElement(ELEMENT_SPAN, tree);
            writer.writeAttribute("class", "butter-component-tree-icon", null);
            if (StringUtils.isNotEmpty(node.getIconPath())) {
                nodeIconsFound = true;
            }
            writer.endElement(ELEMENT_SPAN);

            // title
            writer.startElement(ELEMENT_SPAN, tree);
            writer.writeAttribute("id", tree.getClientId() + "_" + depth + "_" + childNumber, null);
            writer.writeAttribute("class", "butter-component-tree-title", null);
            final Map<String, List<ClientBehavior>> behaviors = tree.getClientBehaviors();
            if (behaviors.containsKey("click")) {
                final String click = behaviors.get("click").get(0).getScript(behaviorContext);

                // could be empty if ajax tag is disabled
                if (StringUtils.isNotEmpty(click)) {
                    final String nodeNumber = "" + nodes.size();
                    nodes.put(nodeNumber, node);
                    final String s = click.replace(",'click',", ",'click_" + nodeNumber + "',");
                    final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(tree.getClientId(), "selectTreeNode({nodeNumber:'" + nodeNumber + "'})");
                    writer.writeAttribute("onclick", s + ";" + jQueryPluginCall, null);
                    writer.writeAttribute("treenode", nodeNumber, null);
                }
            }
            writer.writeText(node.getTitle(), null);
            writer.endElement(ELEMENT_SPAN);

            writer.endElement(ELEMENT_DIV);
        }

        if (!node.isLeaf()) {
            final Collection<Node> subNodes = node.getSubNodes();
            int i = 0;

            for (Node subNode : subNodes) {
                this.encodeNode(tree, writer, behaviorContext, subNode, depth + 1, i++, true);
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

        writer.endElement(ELEMENT_DIV);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlTree htmlTree = (HtmlTree) component;
        final TreeNodeSelectionListener nodeSelectionListener = htmlTree.getNodeSelectionListener();
        final Map<String, List<ClientBehavior>> behaviors = htmlTree.getClientBehaviors();

        if (nodeSelectionListener == null) {
            return;
        }

        if (behaviors.isEmpty()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null) {
            final String[] split = behaviorEvent.split("_");
            final String nodeNumber = split[1];

            final Node node = nodes.get(nodeNumber);

            nodeSelectionListener.processValueChange(new TreeNodeSelectionEvent(selectedNode, node));
            selectedNode = node;
        }
    }

    private String createButterTreeJQueryParameter(final HtmlTree htmlTree) {
        final String expansionClass = getExpansionClass(htmlTree);
        final String collapsingClass = getCollapsingClass(htmlTree);

        final Map<String, List<ClientBehavior>> behaviors = htmlTree.getClientBehaviors();
        final boolean treeIconsEnabled = behaviors.containsKey("click");

        return "{expansionClass: '" + expansionClass +
                "', collapsingClass: '" + collapsingClass +
                "', treeSelectionEnabled: '" + treeIconsEnabled +
                "', treeIconsEnabled: '" + nodeIconsFound + "'}";
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
