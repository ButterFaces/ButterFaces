package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.feature.AutoFocus;
import de.larmic.butterfaces.component.html.feature.Placeholder;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 16.09.14.
 */
public class HtmlAttributePartRenderer {

    public void renderHtmlFeatures(final UIComponent component,
                                   final ResponseWriter writer) throws IOException {
        if (component instanceof AutoFocus) {
            final AutoFocus autoFocus = (AutoFocus) component;

            if (autoFocus.isAutoFocus()) {
                writer.writeAttribute("autofocus", "true", null);
            }
        }

        if (component instanceof Placeholder) {
            final Placeholder placeholder = (Placeholder) component;

            final HtmlAttributePartRenderer htmlAttributePartRenderer = new HtmlAttributePartRenderer();
            htmlAttributePartRenderer.writePlaceholderAttribute(writer, placeholder.getPlaceholder());
        }
    }

    public void writePlaceholderAttribute(final ResponseWriter writer,
                                          final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "placeholder", attributeValue);
    }

    private void writeHtmlAttributeIfNotEmpty(final ResponseWriter writer,
                                              final String attributeName,
                                              final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, attributeName, attributeValue, null);
    }

    private void writeHtmlAttributeIfNotEmpty(final ResponseWriter writer,
                                              final String attributeName,
                                              final String attributeValue,
                                              final String alternativeValue) throws IOException {
        if (attributeValue != null && !"".equals(attributeValue)) {
            writer.writeAttribute(attributeName, attributeValue, attributeName);
        } else if (alternativeValue != null && !"".equals(alternativeValue)) {
            writer.writeAttribute(attributeName, alternativeValue, attributeName);
        }
    }

}