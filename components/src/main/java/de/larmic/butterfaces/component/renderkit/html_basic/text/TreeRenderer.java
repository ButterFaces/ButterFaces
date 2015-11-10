package de.larmic.butterfaces.component.renderkit.html_basic.text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.ajax.JsfAjaxRequest;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.context.StringHtmlEncoder;
import de.larmic.butterfaces.event.TreeNodeExpansionListener;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.resolver.MustacheResolver;

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

        final List<String> mustacheKeys = this.createMustacheKeys(context, tree);

        writer.startElement("script", component);

        writer.writeText("jQuery(function () {\n", null);
        writer.writeText("var entries_" + tree.getClientId().replace(":", "_") + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodes, mustacheKeys, cachedNodes) + ";\n", null);
        final String jQueryBySelector = RenderUtils.createJQueryBySelector(component.getClientId(), "input");
        final String pluginCall = createJQueryPluginCallTrivial(tree, nodes, context);
        writer.writeText("var trivialTree = " + jQueryBySelector + pluginCall + ";", null);

        this.encodeAjaxEvent(tree, writer, "click", "onSelectedEntryChanged");
        this.encodeAjaxEvent(tree, writer, "toggle", "onNodeExpansionStateChanged");

        writer.writeText("});", null);

        writer.endElement("script");

        writer.endElement(ELEMENT_DIV);
    }

    private List<String> createMustacheKeys(FacesContext context, HtmlTree tree) throws IOException {
        if (tree.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, tree.getFacet("template"));
            return MustacheResolver.getMustacheKeysForTree(encodedTemplate);
        }

        return Collections.emptyList();
    }

    private void encodeAjaxEvent(final HtmlTree tree,
                                 final ResponseWriter writer,
                                 final String eventName,
                                 final String trivialCallback) throws IOException {
        final AjaxBehavior ajaxBehavior = JsfAjaxRequest.findFirstActiveAjaxBehavior(tree, eventName);

        if (ajaxBehavior != null) {
            writer.writeText("trivialTree." + trivialCallback + ".addListener(function(node) {", null);
            final String ajaxRequest = new JsfAjaxRequest(tree.getClientId(), true)
                    .setEvent(eventName)
                    .setRender(tree, eventName)
                    .setParams("node.id")
                    .addOnEventHandler(ajaxBehavior.getOnevent())
                    .addOnErrorHandler(ajaxBehavior.getOnerror())
                    .setBehaviorEvent(eventName).toString();
            writer.writeText(ajaxRequest, null);
            writer.writeText("});", null);
        }
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

        if (behaviorEvent != null && "click".equals(behaviorEvent)) {
            try {
                final Integer nodeNumber = Integer.valueOf(params.get("params"));
                final Node node = cachedNodes.get(nodeNumber);
                if (nodeSelectionListener != null) {
                   nodeSelectionListener.processValueChange(new TreeNodeSelectionEvent(selectedNode, node));
                }
                selectedNode = node;
            } catch (NumberFormatException e) {
                // here is nothing to do
            }
        } else if (behaviorEvent != null && "toggle".equals(behaviorEvent)) {
            try {
                final Integer nodeNumber = Integer.valueOf(params.get("params"));
                final Node cachedNode = cachedNodes.get(nodeNumber);
                if (cachedNode.isCollapsed()) {
                    cachedNode.setCollapsed(false);
                    if (nodeExpansionListener != null) {
                        nodeExpansionListener.expandNode(cachedNode);
                    }
                } else {
                    cachedNode.setCollapsed(true);
                    if (nodeExpansionListener != null) {
                        nodeExpansionListener.collapseNode(cachedNode);
                    }
                }
                selectedNode = cachedNode;
            } catch (NumberFormatException e) {
                // here is nothing to do
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
        if (tree.getToManyVisibleItemsRenderDelay() != null || tree.getToManyVisibleItemsThreshold() != null) {
            jQueryPluginCall.append("\n    performanceOptimizationSettings: {");
            if (tree.getToManyVisibleItemsRenderDelay() != null) {
                jQueryPluginCall.append("\n        toManyVisibleItemsRenderDelay: " + tree.getToManyVisibleItemsRenderDelay() + ",");
            }
            if (tree.getToManyVisibleItemsThreshold() != null) {
                jQueryPluginCall.append("\n        toManyVisibleItemsThreshold: " + tree.getToManyVisibleItemsThreshold() + ",");
            }
            jQueryPluginCall.append("\n    },");
        }
        if (tree.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, tree.getFacet("template"));
            jQueryPluginCall.append("\n    templates: ['" + encodedTemplate + "'],");
        } else {
            jQueryPluginCall.append("\n    templates: ['" + DEFAULT_TEMPLATE + "'],");
        }
        jQueryPluginCall.append("\n    entries: entries_" + tree.getClientId().replace(":", "_"));
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
