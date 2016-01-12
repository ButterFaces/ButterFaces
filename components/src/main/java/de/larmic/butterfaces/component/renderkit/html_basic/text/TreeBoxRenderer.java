/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.CachedNodesInitializer;
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

/**
 * @author Lars Michaelis
 */
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

        cachedNodes.putAll(CachedNodesInitializer.createNodesMap(nodes));

        writer.startElement("script", treeBox);
        writer.writeText("jQuery(function () {\n", null);
        writer.writeText("var entries_" + treeBox.getClientId().replace(clientIdSeparator, "_") + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodes, mustacheKeys, cachedNodes) + ";\n", null);
        final String jQueryBySelector = RenderUtils.createJQueryBySelector(treeBox.getClientId(), "input");
        final String pluginCall = createJQueryPluginCallTrivial(treeBox, treeBoxModelType, mustacheKeys);
        writer.writeText("var trivialTree" + treeBox.getClientId().replace(clientIdSeparator, "_") + " = " + jQueryBySelector + pluginCall + ";", null);
        writer.writeText("});", null);
        writer.endElement("script");
    }

    private List<String> createMustacheKeys(FacesContext context, HtmlTreeBox treeBox) throws IOException {
        if (treeBox.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("template"));
            return MustacheResolver.getMustacheKeysForTreeNode(encodedTemplate);
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
        final Node node = cachedNodes.get(selectedIndex);
        return treeBoxModelType == TreeBoxModelType.OBJECTS && node != null ? node.getData() : node;
    }

    private String createJQueryPluginCallTrivial(final HtmlTreeBox treeBox,
                                                 final TreeBoxModelType treeBoxModelType,
                                                 final List<String> mustacheKeys) throws IOException {
        final StringBuilder jQueryPluginCall = new StringBuilder();
        final FacesContext context = FacesContext.getCurrentInstance();

        final Integer selectedEntryId = this.findValueInCachedNodes(treeBox.getValue(), treeBoxModelType);
        final Node selectedNode = selectedEntryId != null ? cachedNodes.get(selectedEntryId) : null;
        final String editable = TrivialComponentsEntriesNodePartRenderer.getEditingMode(treeBox);

        if (treeBoxModelType == TreeBoxModelType.STRINGS || treeBoxModelType == TreeBoxModelType.OBJECTS) {
            jQueryPluginCall.append("TrivialComboBox({");
        } else {
            jQueryPluginCall.append("TrivialTreeComboBox({");
        }
        jQueryPluginCall.append("\n    allowFreeText: false,");
        if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
            jQueryPluginCall.append("\n    valueProperty: 'id',");
        }

        jQueryPluginCall.append("\n    inputTextProperty: '" + StringUtils.getNotNullValue(treeBox.getInputTextProperty(), "title") + "',");

        if (treeBox.getFacet("emptyEntryTemplate") != null) {
            jQueryPluginCall.append("\n    emptyEntryTemplate: '" + StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("emptyEntryTemplate"), "editor-area") + "',");
        } else if (StringUtils.isNotEmpty(treeBox.getPlaceholder())) {
            jQueryPluginCall.append("\n    emptyEntryTemplate: '<div class=\"defaultEmptyEntry\">" + treeBox.getPlaceholder() + "</div>',");
        }

        jQueryPluginCall.append("\n    editingMode: '" + editable + "',");
        if (selectedEntryId != null && selectedNode != null) {
            jQueryPluginCall.append("\n    selectedEntry: " + new TrivialComponentsEntriesNodePartRenderer().renderNode(mustacheKeys, cachedNodes, selectedEntryId, selectedNode) + ",");
        }
        if (treeBox.getFacet("selectedEntryTemplate") != null) {
            jQueryPluginCall.append("\n    selectedEntryTemplate: '" + StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("selectedEntryTemplate"), "editor-area") + "',");
        }
        if (treeBox.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("template"), "editor-area");
            if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
                jQueryPluginCall.append("\n    template: '" + encodedTemplate + "',");
            } else {
                jQueryPluginCall.append("\n    templates: ['" + encodedTemplate + "'],");
            }
        } else if (treeBoxModelType == TreeBoxModelType.NODES) {
            jQueryPluginCall.append("\n    templates: ['" + TreeRenderer.DEFAULT_NODES_TEMPLATE + "'],");
        } else if (treeBoxModelType == TreeBoxModelType.STRINGS || treeBoxModelType == TreeBoxModelType.OBJECTS) {
            jQueryPluginCall.append("\n    template: '" + TreeRenderer.DEFAULT_SINGLE_LINE_OF_TEXT_TEMPLATE + "',");
        }
        jQueryPluginCall.append("\n    spinnerTemplate: '<div class=\"tr-default-spinner\"><div class=\"spinner\"></div><div>" + spinnerText + "</div></div>',");
        jQueryPluginCall.append("\n    noEntriesTemplate: '<div class=\"tr-default-no-data-display\"><div>" + noMatchingText + "</div></div>',");
        jQueryPluginCall.append("\n    entries: entries_" + treeBox.getClientId().replace(":", "_"));
        jQueryPluginCall.append("});");

        return jQueryPluginCall.toString();
    }

    private Integer findValueInCachedNodes(final Object treeBoxValue, final TreeBoxModelType treeBoxModelType) {
        if (treeBoxModelType == TreeBoxModelType.STRINGS && treeBoxValue instanceof String) {
            for (Integer index : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(index);
                if (treeBoxValue.equals(node.getTitle())) {
                    return index;
                }
            }
        } else if (treeBoxModelType == TreeBoxModelType.OBJECTS && treeBoxValue != null) {
            for (Integer index : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(index);
                if (treeBoxValue.equals(node.getData())) {
                    return index;
                }
            }
        } else if (treeBoxValue != null) {
            for (Integer index : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(index);
                if (treeBoxValue.equals(node)) {
                    return index;
                }
            }
        }

        return null;
    }
}
