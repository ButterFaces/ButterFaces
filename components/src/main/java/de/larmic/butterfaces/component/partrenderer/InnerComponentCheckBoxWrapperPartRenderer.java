/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public class InnerComponentCheckBoxWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlCheckBox component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            final StringBuilder defaultStyleClass = new StringBuilder();
            if (component.isHideLabel()) {
                defaultStyleClass.append("butter-component-value-hiddenLabel");
            } else {
                defaultStyleClass.append("butter-component-value");
            }

            defaultStyleClass.append(" butter-component-checkbox");

            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", defaultStyleClass.toString(), null);

            if (!StringUtils.isEmpty(component.getDescription())) {
                writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
                writer.writeAttribute("class", "checkbox withDescription", null);
                writer.startElement("label", component);
            }
        }
    }

    public void renderInnerWrapperEnd(final HtmlCheckBox component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            if (!StringUtils.isEmpty(component.getDescription())) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "butter-component-checkbox-description", null);
                writer.writeText(component.getDescription(), null);
                writer.endElement("span");
                writer.endElement("label");
                writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
            }

            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }
}
