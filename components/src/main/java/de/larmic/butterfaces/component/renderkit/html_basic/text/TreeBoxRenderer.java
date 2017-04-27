/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.ReadonlyPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.CachedNodesInitializer;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.TreeBoxModelType;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.TreeBoxModelWrapper;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.context.StringHtmlEncoder;
import de.larmic.butterfaces.model.tree.EnumTreeBoxWrapper;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.resolver.WebXmlParameters;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.createMustacheKeys;
import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.replaceDotInMustacheKeys;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlTreeBox.COMPONENT_FAMILY, rendererType = HtmlTreeBox.RENDERER_TYPE)
public class TreeBoxRenderer extends AbstractHtmlTagRenderer<HtmlTreeBox> {

    public static final String DEFAULT_SINGLE_LINE_OF_TEXT_TEMPLATE = "<div class=\"tr-template-single-line\">  <div class=\"content-wrapper tr-editor-area\">     <div>{{butterObjectToString}}</div>   </div></div>";

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-treebox");
    }

    @Override
    protected void appendEncodeBegin(final HtmlTreeBox treeBox, ResponseWriter writer) throws IOException {
        final String clientIdSeparator = String.valueOf(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()));
        final String treeBoxReadableId = treeBox.getClientId().replace(clientIdSeparator, "_");
        writer.writeAttribute("data-tree-box-id", treeBoxReadableId, null);
    }

    @Override
    protected void encodeEnd(HtmlTreeBox treeBox, ResponseWriter writer) throws IOException {
        if (!treeBox.isReadonly() || treeBox.getValue() != null) {
            final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);
            final List<Node> nodes = treeBoxModelWrapper.getNodes();
            final TreeBoxModelType treeBoxModelType = treeBoxModelWrapper.getTreeBoxModelType();

            final List<String> mustacheKeys = createMustacheKeys(FacesContext.getCurrentInstance(), treeBox);

            final String clientIdSeparator = String.valueOf(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()));
            final String treeBoxReadableId = treeBox.getClientId().replace(clientIdSeparator, "_");
            final String jQueryBySelector = RenderUtils.createJQueryBySelectorWithoutDot(treeBox.getClientId(), "input");
            final Map<Integer, Node> nodesMap = CachedNodesInitializer.createNodesMap(nodes);
            final String treeOptions = replaceDotInMustacheKeys(mustacheKeys, createTreeOptions(treeBox, treeBoxModelType, mustacheKeys, nodesMap));


            writer.startElement("script", treeBox);
            writer.writeText("jQuery(function () {\n", null);
            writer.writeText("var entries_" + treeBoxReadableId + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodes, replaceDotInMustacheKeys(mustacheKeys), nodesMap) + ";\n", null);
            writer.writeText("ButterFaces.TreeBox.removeTrivialTreeDropDown('" + treeBoxReadableId + "');\n", null);
            writer.writeText("var trivialTreeOptions" + treeBoxReadableId + " = " + treeOptions + ";\n", null);

            if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
                writer.writeText("var trivialTree" + treeBoxReadableId + " = ButterFaces.createTrivialComboBox(" + jQueryBySelector + ",trivialTreeOptions" + treeBoxReadableId + ");\n", null);
            } else {
                writer.writeText("var trivialTree" + treeBoxReadableId + " = ButterFaces.createTrivialTreeComboBox(" + jQueryBySelector + ",trivialTreeOptions" + treeBoxReadableId + ");\n", null);
            }

            writer.writeText("$(trivialTree" + treeBoxReadableId + ".getDropDown()).attr('data-tree-box-id', '" + treeBoxReadableId + "')", null);
            writer.writeText("});", null);
            writer.endElement("script");
        }
    }

    private String createTreeOptions(final HtmlTreeBox treeBox,
                                     final TreeBoxModelType treeBoxModelType,
                                     final List<String> mustacheKeys,
                                     final Map<Integer, Node> nodesMap) throws IOException {
        final StringBuilder options = new StringBuilder();
        final FacesContext context = FacesContext.getCurrentInstance();

        final Integer selectedEntryId = this.findValueInCachedNodes(treeBox.getValue(), treeBoxModelType, nodesMap);
        final Node selectedNode = selectedEntryId != null ? nodesMap.get(selectedEntryId) : null;
        final String editable = TrivialComponentsEntriesNodePartRenderer.getEditingMode(treeBox);

        final WebXmlParameters webXmlParameters = new WebXmlParameters(context.getExternalContext());

        final String noMatchingText = StringUtils.getNotNullValue(treeBox.getNoEntriesText(), webXmlParameters.getNoEntriesText());
        final String spinnerText = StringUtils.getNotNullValue(treeBox.getSpinnerText(), webXmlParameters.getSpinnerText());

        options.append("{");
        options.append("\n    allowFreeText: false,");
        if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
            options.append("\n    valueProperty: 'id',");
        }

        if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
            options.append("\n    inputTextProperty: '" + StringUtils.getNotNullValue(treeBox.getInputTextProperty(), "butterObjectToString") + "',");
        } else {
            options.append("\n    inputTextProperty: '" + StringUtils.getNotNullValue(treeBox.getInputTextProperty(), "title") + "',");
        }

        if (treeBox.getFacet("emptyEntryTemplate") != null) {
            options.append("\n    emptyEntryTemplate: '" + StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("emptyEntryTemplate")) + "',");
        } else if (StringUtils.isNotEmpty(treeBox.getPlaceholder())) {
            options.append("\n    emptyEntryTemplate: '<div class=\"defaultEmptyEntry\">" + treeBox.getPlaceholder() + "</div>',");
        }

        options.append("\n    editingMode: '" + editable + "',");

        options.append("\n    showClearButton: ").append(evaluateShowClearButtonValue(treeBox, webXmlParameters)).append(",");

        if (selectedEntryId != null && selectedNode != null) {
            options.append("\n    selectedEntry: " + new TrivialComponentsEntriesNodePartRenderer().renderNode(mustacheKeys, nodesMap, selectedEntryId, selectedNode) + ",");
        }
        if (treeBox.getFacet("selectedEntryTemplate") != null) {
            options.append("\n    selectedEntryTemplate: '" + StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("selectedEntryTemplate")) + "',");
        }

        // TODO set selectedEntryTemplate?

        if (treeBox.getFacet("template") != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, treeBox.getFacet("template"));
            if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
                options.append("\n    template: '" + encodedTemplate + "',");
            } else {
                options.append("\n    templates: ['" + encodedTemplate + "'],");
            }
        } else if (treeBoxModelType == TreeBoxModelType.NODES) {
            options.append("\n    templates: ['" + TreeRenderer.DEFAULT_NODES_TEMPLATE + "'],");
        } else if (treeBoxModelType == TreeBoxModelType.OBJECTS) {
            options.append("\n    template: '" + DEFAULT_SINGLE_LINE_OF_TEXT_TEMPLATE + "',");
        }
        options.append("\n    spinnerTemplate: '<div class=\"tr-default-spinner\"><div class=\"spinner\"></div><div>" + spinnerText + "</div></div>',");
        options.append("\n    noEntriesTemplate: '<div class=\"tr-default-no-data-display\"><div>" + noMatchingText + "</div></div>',");
        options.append("\n    entries: entries_" + treeBox.getClientId().replace(":", "_"));

        options.append("\n}");

        return options.toString();
    }

    @Override
    protected void encodeInnerEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        if (treeBox.isReadonly()) {
            if (treeBox.getValue() == null) {
                new ReadonlyPartRenderer().renderReadonly(treeBox, writer);
            } else {
                writer.startElement(ELEMENT_DIV, component);
                writer.writeAttribute("class", "butter-component-value", null);
                super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
                writer.endElement(ELEMENT_DIV);
            }
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

        final HtmlTreeBox treeBox = (HtmlTreeBox) component;
        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);
        final TreeBoxModelType treeBoxModelType = treeBoxModelWrapper.getTreeBoxModelType();

        final List<Node> nodes = treeBoxModelWrapper.getNodes();
        final Map<Integer, Node> nodesMap = CachedNodesInitializer.createNodesMap(nodes);

        final Integer selectedIndex = Integer.valueOf(newValue);
        final Node node = nodesMap.get(selectedIndex);
        return treeBoxModelType == (TreeBoxModelType.OBJECTS) && node != null
                ? (node.getData() instanceof EnumTreeBoxWrapper ? ((EnumTreeBoxWrapper) node.getData()).getEnumValue() : node.getData())
                : node;
    }

   boolean evaluateShowClearButtonValue(HtmlTreeBox treeBox, WebXmlParameters webXmlParameters) {
      boolean showClearButtonConf;
      if (treeBox.getShowClearButton() == null) {
          showClearButtonConf = webXmlParameters.isShowTreeBoxClearButton();
      } else {
          showClearButtonConf = treeBox.getShowClearButton();
      }
      return showClearButtonConf;
   }

   private Integer findValueInCachedNodes(final Object treeBoxValue, final TreeBoxModelType treeBoxModelType, final Map<Integer, Node> nodesMap) {
        if (treeBoxModelType == TreeBoxModelType.OBJECTS && treeBoxValue != null) {
            for (Integer index : nodesMap.keySet()) {
                final Node node = nodesMap.get(index);
                if (node.getData() != null && node.getData().equals(treeBoxValue)) {
                    return index;
                }
            }
        } else if (treeBoxValue != null) {
            for (Integer index : nodesMap.keySet()) {
                final Node node = nodesMap.get(index);
                if (node.equals(treeBoxValue)) {
                    return index;
                }
            }
        }

        return null;
    }
}
