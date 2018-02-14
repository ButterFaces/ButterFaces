/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.partrenderer;

import java.io.IOException;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;

import org.butterfaces.component.html.HtmlCheckBox;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.html.HtmlCheckBox;
import org.butterfaces.util.StringUtils;

/**
 * @author Lars Michaelis
 */
public class CheckBoxReadonlyPartRenderer extends ReadonlyPartRenderer {

    @Override
    protected void renderInnerReadonlyPart(UIInput uiComponent, Object value, ResponseWriter writer) throws IOException {
        final HtmlCheckBox checkBox = (HtmlCheckBox) uiComponent;

        final String styleClass = (Boolean) value ? "butter-component-value-readonly-wrapper checked" : "butter-component-value-readonly-wrapper";

        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", styleClass, "styleClass");

        if (checkBox.isSwitch()) {
            writer.startElement("div", checkBox);
            writer.writeAttribute("class", "slider round", "styleClass");
            writer.endElement("div");
            if (StringUtils.isNotEmpty(checkBox.getDescription())) {
                writer.startElement("span", checkBox);
                writer.writeAttribute("class", "butter-component-checkbox-description", "styleClass");
                writer.writeText(checkBox.getDescription(), null);
                writer.endElement("span");
            }
        } else {
            writer.writeText(this.getReadonlyDisplayValue(value, checkBox), null);
        }

        writer.endElement("span");
    }

    /**
     * Should return value string for the readonly view mode. Can be overridden
     * for custom components.
     */
    private String getReadonlyDisplayValue(final Object value, final HtmlCheckBox checkBox) {
        final StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(checkBox.getDescription())) {
            sb.append(checkBox.getDescription()).append(": ");
        }
        sb.append((Boolean) value ? "ja" : "nein");
        return sb.toString();
    }
}
