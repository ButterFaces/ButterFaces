package de.larmic.butterfaces.component.partrenderer;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 16.09.14.
 */
public class HtmlAttributePartRenderer {

    public void writePlaceholderAttribute(final ResponseWriter writer,
                                          final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "placeholder", attributeValue);
    }

    public void writePatternAttribute(final ResponseWriter writer,
                                      final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "pattern", attributeValue);
    }

    public void writeMinAttribute(final ResponseWriter writer,
                                  final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "min", attributeValue);
    }

    public void writeMaxAttribute(final ResponseWriter writer,
                                  final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "max", attributeValue);
    }

    public void writeTypeAttribute(final ResponseWriter writer,
                                   final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "type", attributeValue, "text");
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
