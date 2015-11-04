package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.context.StringHtmlEncoder;
import de.larmic.butterfaces.event.TreeNodeExpansionListener;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.resolver.AjaxRequest;
import de.larmic.butterfaces.resolver.AjaxRequestFactory;
import de.larmic.butterfaces.resolver.JsfAjaxRequestBuilder;
import de.larmic.butterfaces.resolver.MustacheResolver;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

@FacesRenderer(componentFamily = HtmlTree.COMPONENT_FAMILY, rendererType = HtmlTree.RENDERER_TYPE)
public class TreeRenderer extends HtmlBasicRenderer {

    public static final String DEFAULT_TEMPLATE = "<div class=\"tr-template-icon-2-lines tr-tree-entry filterable-item {{styleClass}}\">  <div class=\"img-wrapper {{imageClass}}\" style=\"{{imageStyle}}\"></div>  <div class=\"content-wrapper editor-area\">     <div class=\"main-line\">{{title}}</div>     <div class=\"additional-info\">{{description}}</div>  </div></div>";

    private final Map<Integer, Node> cachedNodes = new HashMap<>();
    private Node selectedNode = null;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final HtmlTree tree = (HtmlTree) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement(ELEMENT_DIV, tree);
        this.writeIdAttribute(context, writer, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree", null);

        writer.startElement("input", tree);

        if (StringUtils.isNotEmpty(tree.getPlaceholder())) {
            writer.writeAttribute(ATTRIBUTE_PLACEHOLDER, tree.getPlaceholder(), null);
        }

        writer.writeAttribute(ATTRIBUTE_CLASS, "butter-component-tree-original-input", null);
        writer.endElement("input");

        cachedNodes.clear();
    }


    @Override
    public void encodeEnd(final FacesContext context,
                          final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final HtmlTree tree = (HtmlTree) component;
        // HINT: getValue() should only called once because getValue() could create a new root node each time
        final Node rootNode = tree.getValue();
        final List<Node> nodes = tree.isHideRootNode() ? rootNode.getSubNodes() : Arrays.asList(rootNode);

        this.initCachedNodes(nodes, 0);

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("script", component);

        writer.writeText("jQuery(function () {", null);
        final String jQueryBySelector = RenderUtils.createJQueryBySelector(component.getClientId(), "input");
        final String pluginCall = createJQueryPluginCallTrivial(tree, nodes, context);
        writer.writeText("var trivialTree = " + jQueryBySelector + pluginCall + ";", null);

        this.encodeAjaxEvent(tree, writer, "click", "onSelectedEntryChanged");
        this.encodeAjaxEvent(tree, writer, "toggle", "onNodeExpansionStateChanged");

        writer.writeText("});", null);

        writer.endElement("script");

        writer.endElement(ELEMENT_DIV);
    }

    private void encodeAjaxEvent(final HtmlTree tree,
                                 final ResponseWriter writer,
                                 final String eventName,
                                 final String trivialCallback) throws IOException {
        final AjaxBehavior ajaxBehavior = findFirstActiveAjaxBehavior(tree.getClientBehaviors().get(eventName));
        if (ajaxBehavior != null && tree.getNodeExpansionListener() != null) {
            writer.writeText("trivialTree." + trivialCallback + ".addListener(function(node) {", null);
            final String test = new JsfAjaxRequestBuilder(tree.getClientId(), true).setRender(tree, eventName).toString();
            final AjaxRequest click = new AjaxRequestFactory().createRequest(tree, eventName, ajaxBehavior.getOnevent(), "node.id");
            final String javaScriptCall = click.createJavaScriptCall();
            writer.writeText(javaScriptCall, null);
            writer.writeText("});", null);
        }
    }

    private AjaxBehavior findFirstActiveAjaxBehavior(final List<ClientBehavior> behaviors) {
        if (behaviors != null) {
            for (ClientBehavior behavior : behaviors) {
                if (behavior instanceof AjaxBehavior && !((AjaxBehavior) behavior).isDisabled()) {
                    return (AjaxBehavior) behavior;
                }
            }
        }

        return null;
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final HtmlTree htmlTree = (HtmlTree) component;
        final TreeNodeSelectionListener nodeSelectionListener = htmlTree.getNodeSelectionListener();
        final TreeNodeExpansionListener nodeExpansionListener = htmlTree.getNodeExpansionListener();
        final Map<String, List<ClientBehavior>> behaviors = htmlTree.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null && "click".equals(behaviorEvent) && nodeSelectionListener != null) {
            try {
                final Integer nodeNumber = Integer.valueOf(params.get("params"));
                final Node node = cachedNodes.get(nodeNumber);
                nodeSelectionListener.processValueChange(new TreeNodeSelectionEvent(selectedNode, node));
                selectedNode = node;
            } catch (NumberFormatException e) {
                // here is nothing to do
                return;
            }
        } else if (behaviorEvent != null && "toggle".equals(behaviorEvent) && nodeExpansionListener != null) {
            try {
                final Integer nodeNumber = Integer.valueOf(params.get("params"));
                final Node node = cachedNodes.get(nodeNumber);
                if (node.isCollapsed()) {
                    node.setCollapsed(false);
                    nodeExpansionListener.expandNode(node);
                } else {
                    node.setCollapsed(true);
                    nodeExpansionListener.collapseNode(node);
                }
                selectedNode = node;
            } catch (NumberFormatException e) {
                // here is nothing to do
                return;
            }
        }
    }

    private String createJQueryPluginCallTrivial(final HtmlTree tree,
                                                 final List<Node> nodes,
                                                 final FacesContext context) throws IOException {
        final StringBuilder jQueryPluginCall = new StringBuilder();
        final String searchBarMode = determineSearchBarMode(tree);
        final Integer selectedNodeNumber = getSelectedNodeNumber(tree);

        if (selectedNodeNumber != null) {
            openPathToNode(cachedNodes.get(selectedNodeNumber), tree.getNodeExpansionListener());
        }

        jQueryPluginCall.append("TrivialTree({");
        jQueryPluginCall.append("\n    searchBarMode: '" + searchBarMode + "',");
        if (selectedNodeNumber != null) {
            jQueryPluginCall.append("\n    selectedEntryId: '" + selectedNodeNumber + "',");
        }
        if (tree.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, tree.getFacet("template"));
            final List<String> mustacheKeys = MustacheResolver.getMustacheKeysForTree(encodedTemplate);
            jQueryPluginCall.append("\n    templates: ['" + encodedTemplate + "'],");
            jQueryPluginCall.append("\n    entries: " + this.renderEntries(nodes, mustacheKeys));
        } else {
            jQueryPluginCall.append("\n    templates: ['" + DEFAULT_TEMPLATE + "'],");
            jQueryPluginCall.append("\n    entries: " + this.renderEntries(nodes, new ArrayList<String>()));
        }
        jQueryPluginCall.append("})");

        return jQueryPluginCall.toString();
    }

    private void openPathToNode(final Node node, final TreeNodeExpansionListener nodeExpansionListener) {
        final Node parent = getParent(node);

        if (parent != null) {
            if (parent.isCollapsed()) {
                parent.setCollapsed(false);
                if (nodeExpansionListener != null) {
                    nodeExpansionListener.expandNode(node);
                }
            }
            openPathToNode(parent, nodeExpansionListener);
        }
    }

    private Node getParent(final Node child) {
        for (Node node : cachedNodes.values()) {
            if (node.getSubNodes().contains(child)) {
                return node;
            }
        }

        return null;
    }

    private String determineSearchBarMode(final HtmlTree tree) {
        if ("show-if-filled".equals(tree.getSearchBarMode())) {
            return "show-if-filled";
        }

        if ("always-visible".equals(tree.getSearchBarMode())) {
            return "always-visible";
        }

        return "none";
    }

    private Integer getSelectedNodeNumber(final HtmlTree tree) {
        if (tree.getNodeSelectionListener() != null) {
            for (Integer nodeNumber : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(nodeNumber);
                if (tree.getNodeSelectionListener().isValueSelected(node)) {
                    return nodeNumber;
                }

            }
        }

        return null;
    }

    private String renderEntries(final List<Node> nodes, final List<String> mustacheKeys) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        new TrivialComponentsEntriesNodePartRenderer().renderNodes(stringBuilder, nodes, 0, mustacheKeys, cachedNodes);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    private int initCachedNodes(final List<Node> nodes,
                                final int index) {
        int newIndex = index;

        for (Node node : nodes) {
            cachedNodes.put(newIndex, node);
            newIndex++;

            if (node.getSubNodes().size() > 0) {
                newIndex = initCachedNodes(node.getSubNodes(), newIndex);
            }
        }

        return newIndex;
    }
}
