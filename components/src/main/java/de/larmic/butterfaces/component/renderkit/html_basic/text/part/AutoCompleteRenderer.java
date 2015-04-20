package de.larmic.butterfaces.component.renderkit.html_basic.text.part;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
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
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlAutoComplete autoComplete = (HtmlAutoComplete) component;

        writer.startElement("div", autoComplete);
        writeIdAttribute(context, writer, autoComplete);
        writer.writeAttribute("class", "butter-autocomplete-component", null);
        if (!autoComplete.getCachedAutoCompleteValues().isEmpty()) {
            writer.startElement("ul", autoComplete);
            for (String value : autoComplete.getCachedAutoCompleteValues()) {
                writer.startElement("li", autoComplete);
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
