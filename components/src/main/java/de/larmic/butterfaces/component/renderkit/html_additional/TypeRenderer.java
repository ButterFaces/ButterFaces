package de.larmic.butterfaces.component.renderkit.html_additional;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 22.10.2014.
 */
public interface TypeRenderer extends HtmlAttributeRenderer {

    default void writeTypeAttribute(final ResponseWriter writer,
                                    final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "type", attributeValue, "text");
    }

}
