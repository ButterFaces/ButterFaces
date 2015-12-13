package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

@FacesRenderer(componentFamily = HtmlTreeBox.COMPONENT_FAMILY, rendererType = HtmlTreeBox.RENDERER_TYPE)
public class TreeBoxRenderer extends AbstractHtmlTagRenderer<HtmlTreeBox> {

    private final Map<Integer, Node> cachedNodes = new HashMap<>();

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-treebox");
    }

    @Override
    protected void encodeEnd(HtmlTreeBox treeBox, ResponseWriter writer) throws IOException {
        final Node rootNode = treeBox.getValues();
        final List<Node> nodes = treeBox.isHideRootNode() ? rootNode.getSubNodes() : Arrays.asList(rootNode);

        this.initCachedNodes(nodes, 0);

        writer.startElement("script", treeBox);
        writer.writeText("jQuery(function () {\n", null);
        writer.writeText("var entries_" + treeBox.getClientId().replace(":", "_") + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodes, Collections.<String>emptyList(), cachedNodes)+";\n", null);
        final String jQueryBySelector = RenderUtils.createJQueryBySelector(treeBox.getClientId(), "input");
        final String pluginCall = createJQueryPluginCallTrivial(treeBox);
        writer.writeText("var trivialTree"+ treeBox.getClientId().replace(":", "_") + " = " + jQueryBySelector + pluginCall + ";", null);
        writer.writeText("});", null);
        writer.endElement("script");
    }

    @Override
    protected void encodeInnerEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        if (treeBox.isReadonly()) {
            writer.startElement(ELEMENT_DIV, component);
            writer.writeAttribute("class", "butter-component-value", null);
            super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
            writer.endElement(ELEMENT_DIV);
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context,
                                    final UIComponent component,
                                    final Object submittedValue) throws ConverterException {
        if (submittedValue == null || "".equals(submittedValue)) {
            return null;
        }

        final String newValue = (String) submittedValue;
        final Integer selectedIndex = Integer.valueOf(newValue);
        return cachedNodes.get(selectedIndex);
    }

    private String createJQueryPluginCallTrivial(final HtmlTreeBox treeBox) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        Integer selectedEntryId = null;
        Node selectedNode = null;

        if (treeBox.getValue() != null) {
            for (Integer index : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(index);
                if (treeBox.getValue().equals(node)) {
                    selectedEntryId = index;
                    selectedNode = node;
                    break;
                }
            }
        }

        final String editable = TrivialComponentsEntriesNodePartRenderer.getEditingMode(treeBox);

        jQueryPluginCall.append("TrivialTreeComboBox({");
        jQueryPluginCall.append("\n    allowFreeText: true,");
        jQueryPluginCall.append("\n    inputTextProperty: 'title',");
        if (StringUtils.isNotEmpty(treeBox.getPlaceholder())) {
            jQueryPluginCall.append("\n    emptyEntry: {");
            jQueryPluginCall.append("\n    \"title\": \"" + treeBox.getPlaceholder() + "\"");
            //jQueryPluginCall.append("\n    \"imageUrl\": \"-\",");
            //jQueryPluginCall.append("\n    \"additionalInfo\": \"\"");
            jQueryPluginCall.append("\n    },");
        }
        jQueryPluginCall.append("\n    editingMode: '" + editable + "',");
        if (selectedEntryId != null && selectedNode != null) {
            jQueryPluginCall.append("\n    selectedEntry: " + new TrivialComponentsEntriesNodePartRenderer().renderNode(Collections.<String>emptyList(), cachedNodes, selectedEntryId, selectedNode) + ",");
        }
        jQueryPluginCall.append("\n    templates: ['" + TreeRenderer.DEFAULT_TEMPLATE + "'],");
        jQueryPluginCall.append("\n    entries: entries_" + treeBox.getClientId().replace(":", "_"));
        jQueryPluginCall.append("});");

        return jQueryPluginCall.toString();
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
