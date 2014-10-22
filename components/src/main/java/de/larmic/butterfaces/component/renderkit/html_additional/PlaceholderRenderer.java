package de.larmic.butterfaces.component.renderkit.html_additional;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 22.10.2014.
 */
public interface PlaceholderRenderer extends HtmlAttributeRenderer {

    default void writePlaceholderAttribute(final ResponseWriter writer,
                                           final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "placeholder", attributeValue);
    }

}
