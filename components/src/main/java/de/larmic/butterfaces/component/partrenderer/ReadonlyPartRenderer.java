/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public class ReadonlyPartRenderer {

    public void renderReadonly(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final boolean readonly = component.isReadonly();
        final Object value = component.getValue();

        if (readonly) {
            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, uiComponent);
            final StringBuilder sb = new StringBuilder("butter-component-value butter-component-value-readonly");
            if (component.isHideLabel()) {
                sb.append(" butter-component-value-hiddenLabel");
            }
            writer.writeAttribute("class", sb.toString(), null);
            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "butter-component-value-readonly-wrapper", "styleClass");
            writer.writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
            writer.endElement("span");
            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }

    /**
     * Should return value string for the readonly view mode. Can be overridden
     * for custom components.
     */
    private String getReadonlyDisplayValue(final Object value, final UIInput component, final Converter converter) {
        if (value == null || "".equals(value)) {
            return "-";
        } else if (converter != null) {
            final String asString = converter.getAsString(FacesContext.getCurrentInstance(), component, value);
            return asString == null ? "-" : asString;
        }

        if (component instanceof HtmlCheckBox) {
            HtmlCheckBox checkBoxComponent = (HtmlCheckBox) component;
            final StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotEmpty(checkBoxComponent.getDescription())) {
                sb.append(checkBoxComponent.getDescription()).append(": ");
            }
            sb.append((Boolean) value ? "ja" : "nein");
            return sb.toString();
        }

        return String.valueOf(value);
    }
}
