package de.larmic.butterfaces.component.renderkit.html_additional;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 22.10.2014.
 */
public interface HtmlAttributeRenderer {

    default void writeHtmlAttributeIfNotEmpty(final ResponseWriter writer,
                                              final String attributeName,
                                              final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, attributeName, attributeValue, null);
    }

    default void writeHtmlAttributeIfNotEmpty(final ResponseWriter writer,
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
