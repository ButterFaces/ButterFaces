package de.larmic.butterfaces.component.renderkit.html_additional;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 22.10.2014.
 */
public interface MinMaxNumberRenderer extends HtmlAttributeRenderer {

    default void writeMinAttribute(final ResponseWriter writer,
                                   final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "min", attributeValue);
    }

    default void writeMaxAttribute(final ResponseWriter writer,
                                   final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "max", attributeValue);
    }
}
