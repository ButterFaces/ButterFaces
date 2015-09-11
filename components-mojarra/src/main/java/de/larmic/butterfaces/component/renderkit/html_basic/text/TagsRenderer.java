package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.text.HtmlTags;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@FacesRenderer(componentFamily = HtmlTags.COMPONENT_FAMILY, rendererType = HtmlTags.RENDERER_TYPE)
public class TagsRenderer extends AbstractTextRenderer<HtmlTags> {

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
            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", "butter-component-value", null);
            super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTags htmlTags = (HtmlTags) component;

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", createJQueryPluginCallTivial(htmlTags)), null);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), null, "_butterTagsInit();"), null);
        writer.endElement("script");
    }

    private String createJQueryPluginCallTivial(final HtmlTags tags) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        final String editable = getEditingMode(tags);

        jQueryPluginCall.append("TrivialTagBox({");
        jQueryPluginCall.append("\n    autoComplete: false,");
        jQueryPluginCall.append("\n    allowFreeText: true,");
        jQueryPluginCall.append("\n    showTrigger: false,");
        jQueryPluginCall.append("\n    distinct: " + tags.isDistinct() + ",");
        jQueryPluginCall.append("\n    editingMode: '" + editable + "',");
        if (tags.getMaxTags() != null) {
            jQueryPluginCall.append("\n    maxSelectedEntries: " + tags.getMaxTags() + ",");
        }
        final String selectedEntries = this.getSelectedEntries(tags);
        if (StringUtils.isNotEmpty(selectedEntries)) {
            jQueryPluginCall.append("\n    selectedEntries: [" + selectedEntries + "],");
        }
        jQueryPluginCall.append("\n    valueProperty: 'displayValue',");
        jQueryPluginCall.append("\n    template: TrivialComponents.singleLineTemplate,");
        //jQueryPluginCall.append("\n    freeTextSeparators: ['" + tags.getConfirmKeys() + "']");
        jQueryPluginCall.append("\n    freeTextSeparators: [',',' '],");
        jQueryPluginCall.append("\n    valueSeparator: [',']");
        jQueryPluginCall.append("});");

        return jQueryPluginCall.toString();
    }

    private String getSelectedEntries(final HtmlTags tags) {
        if (tags.getValue() == null) {
            return null;
        }

        final String componentValue = tags.getValue().toString();

        final Iterator<String> iterator = new ArrayList<>(Arrays.asList(componentValue.split(",| "))).iterator();

        final StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            final String next = iterator.next();
            if (StringUtils.isNotEmpty(next)) {
                sb.append("{displayValue:'" + next + "'}");
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }

    private String getEditingMode(final HtmlTags tags) {
        if (tags.isReadonly()) {
            return "readonly";
        } else if (tags.isDisabled()) {
            return "disabled";
        }

        return "editable";
    }
}
