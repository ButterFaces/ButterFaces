/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.partrenderer;

import java.io.IOException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.HtmlInputComponent;

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
            final StringBuilder sb = new StringBuilder(Constants.COMPONENT_VALUE_CLASS);
            sb.append(" butter-component-value-readonly mt-2");
            if (component.isHideLabel()) {
                sb.append(" butter-component-value-hiddenLabel");
            }
            writer.writeAttribute("class", sb.toString(), null);

            this.renderInnerReadonlyPart(uiComponent, value, writer);

            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }

    protected void renderInnerReadonlyPart(final UIInput uiComponent, final Object value, final ResponseWriter writer) throws IOException {
        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "butter-component-value-readonly-wrapper", "styleClass");
        writer.writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
        writer.endElement("span");
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

        return String.valueOf(value);
    }
}
