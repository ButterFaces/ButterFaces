package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.TreeBoxModelType;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.TreeBoxModelWrapper;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.context.StringHtmlEncoder;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.resolver.MustacheResolver;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FacesRenderer(componentFamily = HtmlTreeBox.COMPONENT_FAMILY, rendererType = HtmlTreeBox.RENDERER_TYPE)
public class TreeBoxRenderer extends AbstractHtmlTagRenderer<HtmlTreeBox> {

    public static final String DEFAULT_SPINNER_TEXT = "Fetching data...";
    public static final String DEFAULT_NO_MATCHING_TEXT = "No matching entries...";

    private final Map<Integer, Node> cachedNodes = new HashMap<>();

    private String noMatchingText;
    private String spinnerText;
    private TreeBoxModelType treeBoxModelType;

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-treebox");


        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        treeBoxModelType = null;
        noMatchingText = StringUtils.getNotNullValue(treeBox.getNoEntriesText(), DEFAULT_NO_MATCHING_TEXT);
        spinnerText = StringUtils.getNotNullValue(treeBox.getSpinnerText(), DEFAULT_SPINNER_TEXT);
    }

    @Override
    protected void encodeEnd(HtmlTreeBox treeBox, ResponseWriter writer) throws IOException {
        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);
        final List<Node> nodes = treeBoxModelWrapper.getNodes();
        treeBoxModelType = treeBoxModelWrapper.getTreeBoxModelType();

        final List<String> mustacheKeys = this.createMustacheKeys(FacesContext.getCurrentInstance(), treeBox);

        final String clientIdSeparator = String.valueOf(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()));

        this.initCachedNodes(nodes, 0);

        writer.startElement("script", treeBox);
        writer.writeText("jQuery(function () {\n", null);
        writer.writeText("var entries_" + treeBox.getClientId().replace(clientIdSeparator, "_") + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodes, mustacheKeys, cachedNodes) + ";\n", null);
        final String jQueryBySelector = RenderUtils.createJQueryBySelector(treeBox.getClientId(), "input");
        final String pluginCall = createJQueryPluginCallTrivial(treeBox, treeBoxModelType);
        writer.writeText("var trivialTree" + treeBox.getClientId().replace(clientIdSeparator, "_") + " = " + jQueryBySelector + pluginCall + ";", null);
        writer.writeText("});", null);
        writer.endElement("script");
    }

    private List<String> createMustacheKeys(FacesContext context, HtmlTreeBox treeBox) throws IOException {
        if (treeBox.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("template"));
            return MustacheResolver.getMustacheKeysForTree(encodedTemplate);
        }

        return Collections.emptyList();
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

        if (treeBoxModelType == TreeBoxModelType.STRINGS) {
            return newValue;
        }

        final Integer selectedIndex = Integer.valueOf(newValue);
        return cachedNodes.get(selectedIndex);
    }

    private String createJQueryPluginCallTrivial(final HtmlTreeBox treeBox,
                                                 final TreeBoxModelType treeBoxModelType) throws IOException {
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

        if (treeBoxModelType == TreeBoxModelType.STRINGS) {
            jQueryPluginCall.append("TrivialComboBox({");
        } else {
            jQueryPluginCall.append("TrivialTreeComboBox({");
        }
        jQueryPluginCall.append("\n    allowFreeText: true,");
        jQueryPluginCall.append("\n    inputTextProperty: 'title',");
        if (StringUtils.isNotEmpty(treeBox.getPlaceholder())) {
            jQueryPluginCall.append("\n    emptyEntry: {");
            // TODO BUT-433 this does not work when use custom templates. it only replaces title attribute (if exists).
            // TODO see https://github.com/trivial-components/trivial-components/issues/27
            jQueryPluginCall.append("\n    \"title\": \"" + treeBox.getPlaceholder() + "\"");
            //jQueryPluginCall.append("\n    \"imageUrl\": \"-\",");
            //jQueryPluginCall.append("\n    \"additionalInfo\": \"\"");
            jQueryPluginCall.append("\n    },");
        }
        jQueryPluginCall.append("\n    editingMode: '" + editable + "',");
        if (selectedEntryId != null && selectedNode != null) {
            jQueryPluginCall.append("\n    selectedEntry: " + new TrivialComponentsEntriesNodePartRenderer().renderNode(Collections.<String>emptyList(), cachedNodes, selectedEntryId, selectedNode) + ",");
        }
        if (treeBox.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(FacesContext.getCurrentInstance(), treeBox.getFacet("template"), "editor-area");
            jQueryPluginCall.append("\n    templates: ['" + encodedTemplate + "'],");
        } else if (treeBoxModelType == TreeBoxModelType.NODES) {
            jQueryPluginCall.append("\n    templates: ['" + TreeRenderer.DEFAULT_NODES_TEMPLATE + "'],");
        } else if (treeBoxModelType == TreeBoxModelType.STRINGS) {
            jQueryPluginCall.append("\n    template: '" + TreeRenderer.DEFAULT_SINGLE_LINE_OF_TEXT_TEMPLATE + "',");
        }
        jQueryPluginCall.append("\n    spinnerTemplate: '<div class=\"tr-default-spinner\"><div class=\"spinner\"></div><div>" + spinnerText + "</div></div>',");
        jQueryPluginCall.append("\n    noEntriesTemplate: '<div class=\"tr-default-no-data-display\"><div>" + noMatchingText + "</div></div>',");
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
