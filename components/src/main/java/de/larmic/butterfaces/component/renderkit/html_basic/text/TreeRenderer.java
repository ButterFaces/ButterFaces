package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.tree.HtmlTree;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
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

    private static final String DEFAULT_TEMPLATE = "<div class=\"tr-template-icon-2-lines tr-tree-entry filterable-item {{styleClass}}\">  <div class=\"img-wrapper\" style=\"{{imageStyle}}\"></div>  <div class=\"content-wrapper editor-area\">     <div class=\"main-line\">{{title}}</div>     <div class=\"additional-info\">{{description}}</div>  </div></div>";

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
        final ResponseWriter writer = context.getResponseWriter();

        final List<ClientBehavior> clicks = tree.getClientBehaviors().get("click");

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), "input", createJQueryPluginCallTivial(tree)), null);

        if (clicks != null && !clicks.isEmpty()) {
            // TODO check ajax disabled
            final ClientBehavior clientBehavior = clicks.get(0);

            if (clientBehavior instanceof AjaxBehavior) {
                final AjaxBehavior ajaxBehavior = (AjaxBehavior) clientBehavior;
                if (!ajaxBehavior.isDisabled()) {
                    final String renderIds = createRenderIds(tree, ajaxBehavior.getRender());
                    writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-component-tree-original-input", "selectTreeNodeNew('" + renderIds + "')"), null);
                }
            }

        }
        writer.endElement("script");

        writer.endElement(ELEMENT_DIV);
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
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

        if (behaviorEvent != null && "click".equals(behaviorEvent)) {
            try {
                final Integer nodeNumber = Integer.valueOf(params.get("params"));
                final Node node = cachedNodes.get(nodeNumber);
                nodeSelectionListener.processValueChange(new TreeNodeSelectionEvent(selectedNode, node));
                selectedNode = node;
            } catch (NumberFormatException e) {
                // here is nothing to do
                return;
            }
        }
    }

    private String createJQueryPluginCallTivial(final HtmlTree tree) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        jQueryPluginCall.append("TrivialTree({");
        jQueryPluginCall.append("\n    showSearchField: false,");
        jQueryPluginCall.append("\n    templates: ['" + DEFAULT_TEMPLATE + "'],");
        jQueryPluginCall.append("\n    entries: " + this.renderEntries(tree));
        jQueryPluginCall.append("})");

        return jQueryPluginCall.toString();
    }

    private String renderEntries(final HtmlTree tree) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Node rootNode = tree.getValue();

        stringBuilder.append("[");
        renderNodes(stringBuilder, tree.isHideRootNode() ? rootNode.getSubNodes() : Arrays.asList(rootNode), 0);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    private int renderNodes(final StringBuilder stringBuilder,
                            final List<Node> nodes,
                            final int index) {
        int newIndex = index;

        final Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()) {
            final Node node = iterator.next();
            stringBuilder.append("{");
            stringBuilder.append("\"id\": " + newIndex + ",");
            if (StringUtils.isNotEmpty(node.getStyleClass())) {
                stringBuilder.append("\"styleClass\": \"" + node.getStyleClass() + "\",");
            }
            if (StringUtils.isNotEmpty(node.getImageIcon())) {
                stringBuilder.append("\"imageStyle\": \"background-image: url(" + node.getImageIcon() + ")\",");
            } else {
                stringBuilder.append("\"imageStyle\": \"display:none\",");
            }
            stringBuilder.append("\"title\": \"" + node.getTitle() + "\"");

            cachedNodes.put(newIndex, node);

            newIndex++;

            if (node.getSubNodes().size() > 0) {
                stringBuilder.append(",\"children\": [");
                newIndex = renderNodes(stringBuilder, node.getSubNodes(), newIndex);
                stringBuilder.append("]");
            }

            stringBuilder.append("}");

            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }

        return newIndex;
    }

    private static String createRenderIds(final UIComponent component,
                                          final Collection<String> ids) {
        final StringBuilder builder = new StringBuilder();

        if ((null == ids) || ids.isEmpty()) {
            builder.append('0');
            return builder.toString();
        }

        boolean first = true;

        for (String id : ids) {
            if (id.trim().length() == 0) {
                continue;
            }
            if (!first) {
                builder.append(' ');
            } else {
                first = false;
            }

            if (id.equals("@all") || id.equals("@none") || id.equals("@form") || id.equals("@this")) {
                builder.append(id);
            } else {
                builder.append(getResolvedId(component, id));
            }
        }

        return builder.toString();
    }

    // Returns the resolved (client id) for a particular id.
    private static String getResolvedId(UIComponent component, String id) {

        UIComponent resolvedComponent = component.findComponent(id);
        if (resolvedComponent == null) {
            if (id.charAt(0) == UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())) {
                return id.substring(1);
            }
            return id;
        }

        return resolvedComponent.getClientId();
    }
}
