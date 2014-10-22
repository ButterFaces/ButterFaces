package de.larmic.butterfaces.component.renderkit.html_additional;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 22.10.2014.
 */
public interface PatternRenderer extends HtmlAttributeRenderer {

    default void writePatternAttribute(final ResponseWriter writer,
                                       final String attributeValue) throws IOException {
        writeHtmlAttributeIfNotEmpty(writer, "pattern", attributeValue);
    }

}
