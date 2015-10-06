package de.larmic.butterfaces.component.renderkit.html_basic.tree;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 24.10.14.
 */
//@FacesRenderer(componentFamily = HtmlTree.COMPONENT_FAMILY, rendererType = HtmlTree.RENDERER_TYPE)
public class OldTreeRenderer extends HtmlBasicRenderer {

    private final Map<String, Node> nodes = new HashMap<>();
    private Node selectedNode = null;
    private boolean nodeIconsFound = false;

    private WebXmlParameters webXmlParameters;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final HtmlTree htmlTree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        webXmlParameters = new WebXmlParameters(context.getExternalContext());

        writer.startElement(ELEMENT_DIV, htmlTree);
        this.writeIdAttribute(context, writer, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree", null);

        final ClientBehaviorContext behaviorContext =
                ClientBehaviorContext.createClientBehaviorContext(context,
                        htmlTree, "click", component.getClientId(context), null);

        final Node root = htmlTree.getValue();

        nodeIconsFound = false;

        this.encodeNode(htmlTree, writer, behaviorContext, root, 0, 0, false, !htmlTree.isHideRootNode());
    }

    private void encodeNode(final HtmlTree tree,
                            final ResponseWriter writer,
                            final ClientBehaviorContext behaviorContext,
                            final Node node,
                            final int depth,
                            final int childNumber,
                            final boolean collapsed,
                            final boolean renderNode) throws IOException {
        if (renderNode) {
            writer.startElement("ul", tree);
            writer.startElement("li", tree);

            final boolean nodeSelected = tree.getNodeSelectionListener() != null && tree.getNodeSelectionListener().isValueSelected(node);
            final String styleClass = StringUtils.getNullSafeValue(node.getStyleClass())
                    + (nodeSelected ? " butter-component-tree-node-selected butter-tree-node" : " butter-tree-node");

            writer.writeAttribute("class", styleClass, null);

            final String nodeNumber = "" + nodes.size();
            nodes.put(nodeNumber, node);

            if (collapsed) {
                writer.writeAttribute(ATTRIBUTE_STYLE, "display: none;", null);
            }

            writer.startElement(ELEMENT_DIV, tree);
            writer.writeAttribute("class", "butter-component-tree-row", null);

            // collapse
            writer.startElement(ELEMENT_SPAN, tree);
            final String collapsingClass = node.isCollapsed() ? webXmlParameters.getExpansionGlyphicon() : webXmlParameters.getCollapsingGlyphicon();
            final String nodeClass = node.isLeaf() ? "butter-component-tree-leaf" : "butter-component-tree-node " + collapsingClass;
            writer.writeAttribute("class", "butter-component-tree-jquery-marker " + nodeClass, null);

            final Map<String, List<ClientBehavior>> behaviors = tree.getClientBehaviors();
            if (behaviors.containsKey("click")) {
                final String click = behaviors.get("click").get(0).getScript(behaviorContext);

                if (StringUtils.isNotEmpty(click)) {
                    final String s = click.replace(",'click',", ",'collapse_" + nodeNumber + "',");
                    writer.writeAttribute("onclick", s, null);
                }
            }

            writer.endElement(ELEMENT_SPAN);

            // icon
            writer.startElement(ELEMENT_SPAN, tree);
            if (StringUtils.isNotEmpty(node.getImageIcon())) {
                writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree-icon", null);
                writer.writeAttribute(ATTRIBUTE_STYLE, "background-image:url(" + node.getImageIcon() + ")", null);
                nodeIconsFound = true;
            } else if (StringUtils.isNotEmpty(node.getGlyphiconIcon())) {
                writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree-icon " + node.getGlyphiconIcon(), null);
                nodeIconsFound = true;
            } else {
                writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree-icon", null);
            }

            writer.endElement(ELEMENT_SPAN);

            // title
            writer.startElement(ELEMENT_SPAN, tree);
            writer.writeAttribute("id", tree.getClientId() + "_" + depth + "_" + childNumber, null);
            writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree-title", null);

            if (behaviors.containsKey("click")) {
                final String click = behaviors.get("click").get(0).getScript(behaviorContext);

                // could be empty if ajax tag is disabled
                if (StringUtils.isNotEmpty(click)) {
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
                this.encodeNode(tree, writer, behaviorContext, subNode, depth + 1, i++, isCollapsed(node) || collapsed, true);
            }
        }

        if (renderNode) {
            writer.endElement("li");
            writer.endElement("ul");
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
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

        if (behaviorEvent != null && behaviorEvent.contains("_")) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            final String nodeNumber = split[1];

            final Node node = nodes.get(nodeNumber);

            if (node != null) {
                if ("click".equals(event)) {
                    nodeSelectionListener.processValueChange(new TreeNodeSelectionEvent(selectedNode, node));
                    selectedNode = node;
                } else if ("collapse".equals(event)) {
                    node.setCollapsed(!node.isCollapsed());
                }
            }
        }
    }

    private String createButterTreeJQueryParameter(final HtmlTree htmlTree) {
        final Map<String, List<ClientBehavior>> behaviors = htmlTree.getClientBehaviors();
        final boolean treeIconsEnabled = behaviors.containsKey("click");

        return "{expansionClass: '" + webXmlParameters.getExpansionGlyphicon() +
                "', collapsingClass: '" + webXmlParameters.getCollapsingGlyphicon() +
                "', treeSelectionEnabled: '" + treeIconsEnabled +
                "', treeIconsEnabled: '" + nodeIconsFound + "'}";
    }

    private boolean isCollapsed(final Node node) {
        return !node.getSubNodes().isEmpty() && node.isCollapsed();
    }
}
