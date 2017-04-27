package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTags;
import de.larmic.butterfaces.component.partrenderer.ReadonlyPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.CachedNodesInitializer;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.model.tree.DefaultNodeImpl;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.util.StringJoiner;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.FreeTextSeparators.getFreeTextSeparators;
import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.createMustacheKeys;
import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.replaceDotInMustacheKeys;
import static de.larmic.butterfaces.util.StringUtils.joinWithCommaSeparator;

@FacesRenderer(componentFamily = HtmlTags.COMPONENT_FAMILY, rendererType = HtmlTags.RENDERER_TYPE)
public class TagsRenderer extends AbstractHtmlTagRenderer<HtmlTags> {

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-tags");
    }

    @Override
    protected void encodeInnerEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTags htmlTags = (HtmlTags) component;

        if (htmlTags.isReadonly()) {
            if (htmlTags.getValue() == null || StringUtils.isEmpty((String) htmlTags.getValue())) {
                new ReadonlyPartRenderer().renderReadonly(htmlTags, writer);
            } else {
                writer.startElement(ELEMENT_DIV, component);
                writer.writeAttribute("class", "butter-component-value", null);
                super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
                writer.endElement(ELEMENT_DIV);
            }
        }
    }

    @Override
    protected void encodeEnd(HtmlTags htmlTags, ResponseWriter writer) throws IOException {
        writer.startElement("script", htmlTags);

        final String clientIdSeparator = String.valueOf(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()));
        final String treeBoxReadableId = htmlTags.getClientId().replace(clientIdSeparator, "_");
        final List<Node> entries = createEntries(htmlTags.getEntries());

        writer.writeText("jQuery(function () {\n", null);
        if (!entries.isEmpty()) {
            final List<String> mustacheKeys = createMustacheKeys(FacesContext.getCurrentInstance(), htmlTags);
            final Map<Integer, Node> nodesMap = CachedNodesInitializer.createNodesMap(entries);
            writer.writeText("var entries_" + treeBoxReadableId + " = " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(entries, replaceDotInMustacheKeys(mustacheKeys), nodesMap) + ";\n", null);
        }

        final String jQueryBySelector = RenderUtils.createJQueryBySelectorWithoutDot(htmlTags.getClientId(), ".butter-input-component");
        writer.writeText("var trivialTagsJQueryObject" + treeBoxReadableId + " = " + jQueryBySelector + ";\n", null);
        writer.writeText("var trivialTagsOptions" + treeBoxReadableId + " = " + createTagOptions(htmlTags) + ";\n", null);
        writer.writeText("var trivialTags" + treeBoxReadableId + " = ButterFaces.createTrivialTagComponent(" + jQueryBySelector + ",trivialTagsOptions" + treeBoxReadableId + ");\n", null);

        //final String pluginCall = createJQueryPluginCallTrivial(htmlTags, entries.isEmpty() ? null : "entries_" + treeBoxReadableId);
        //writer.writeText("var trivialTags" + treeBoxReadableId + " = " + jQueryBySelector + pluginCall + "\n", null);
        //writer.writeText(RenderUtils.createJQueryBySelector(htmlTags.getClientId(), null) + "_butterTagsInit(); \n", null);

        writer.writeText("});", null);

        writer.endElement("script");
    }

    private String createTagOptions(final HtmlTags tags) {
        final StringBuilder options = new StringBuilder();

        final String editable = TrivialComponentsEntriesNodePartRenderer.getEditingMode(tags);

        options.append("{");
        options.append("\n    autoComplete: " + tags.isAutoComplete() + ",");
        options.append("\n    distinct: " + tags.isDistinct() + ",");
        options.append("\n    editingMode: '" + editable + "',");

        if (tags.getMaxTags() != null) {
            options.append("\n    maxSelectedEntries: " + tags.getMaxTags() + ",");
        }

        final String selectedEntries = this.getSelectedEntries(tags);
        if (StringUtils.isNotEmpty(selectedEntries)) {
            options.append("\n    selectedEntries: [" + selectedEntries + "],");
        }

        options.append("\n    freeTextSeparators: " + createFreeTextSeparators(tags) + ",");

        options.append("\n}");

        return options.toString();
    }

    // TODO fix Ös

    private List<Node> createEntries(final List<Object> objects) {
        final List<Node> entries = new ArrayList<>();

        if (objects != null) {
            for (Object object : objects) {
                entries.add(new DefaultNodeImpl(null, object));
            }
        }

        return entries;
    }

    private String createFreeTextSeparators(final HtmlTags tags) {
        return "[" + joinWithCommaSeparator(getFreeTextSeparators(tags), true) + "]";
    }

    private String getSelectedEntries(final HtmlTags tags) {
        final String componentValue = getSubmittedValueOrValue(tags);

        if (StringUtils.isNotEmpty(componentValue)) {
            final List<String> freeTextSeparators = getFreeTextSeparators(tags);
            final String valueSplitter = StringJoiner.on("|").join(freeTextSeparators).toString();
            final Iterator<String> iterator = new ArrayList<>(Arrays.asList(componentValue.split(valueSplitter))).iterator();

            final StringBuilder sb = new StringBuilder();

            while (iterator.hasNext()) {
                final String next = iterator.next();
                if (StringUtils.isNotEmpty(next)) {
                    sb.append("{displayValue:'" + escapeDisplayValue(next) + "'}");
                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                }
            }

            return sb.toString();
        }

        return null;
    }

    private String escapeDisplayValue(String value) {
        return value.replaceAll("'", "\\\\'");
    }

    private String getSubmittedValueOrValue(final HtmlTags tags) {
        if (tags.getSubmittedValue() != null) {
            return tags.getSubmittedValue().toString();
        }

        if (tags.getValue() != null) {
            return tags.getValue().toString();
        }

        return null;
    }
}
