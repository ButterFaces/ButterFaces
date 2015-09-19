package de.larmic.butterfaces.component.renderkit.html_basic.text.part;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.base.renderer.HtmlDeprecatedBasicRenderer;
import de.larmic.butterfaces.component.html.text.part.HtmlAutoComplete;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlAutoComplete.COMPONENT_FAMILY, rendererType = HtmlAutoComplete.RENDERER_TYPE)
public class AutoCompleteRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlAutoComplete autoComplete = (HtmlAutoComplete) component;

        writer.startElement(HtmlDeprecatedBasicRenderer.ELEMENT_DIV, autoComplete);
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
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement(HtmlDeprecatedBasicRenderer.ELEMENT_DIV);
    }
}
