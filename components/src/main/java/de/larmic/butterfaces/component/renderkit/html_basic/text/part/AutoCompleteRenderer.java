package de.larmic.butterfaces.component.renderkit.html_basic.text.part;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.text.part.HtmlAutoComplete;

@FacesRenderer(componentFamily = HtmlAutoComplete.COMPONENT_FAMILY, rendererType = HtmlAutoComplete.RENDERER_TYPE)
public class AutoCompleteRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlAutoComplete autoComplete = (HtmlAutoComplete) component;

        writer.startElement("div", autoComplete);
        writeIdAttribute(context, writer, autoComplete);
        writer.writeAttribute("class", "butter-component-autocomplete butter-dropdownlist-container", null);
        if (!autoComplete.getCachedAutoCompleteValues().isEmpty()) {
            writer.startElement("ul", autoComplete);
            writer.writeAttribute("class", "butter-dropdownlist-resultList", null);
            for (String value : autoComplete.getCachedAutoCompleteValues()) {
                writer.startElement("li", autoComplete);
                writer.writeAttribute("class", "butter-dropdownlist-resultItem", null);
                writer.writeAttribute("data-select-value", value, null);
                writer.writeText(value, autoComplete, null);
                writer.endElement("li");
            }
            writer.endElement("ul");
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");
    }
}
