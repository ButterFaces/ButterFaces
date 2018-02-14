/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.partrenderer;

import java.io.IOException;
import javax.faces.context.ResponseWriter;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.HtmlCheckBox;
import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

/**
 * @author Lars Michaelis
 */
public class InnerComponentCheckBoxWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlCheckBox component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            final StringBuilder defaultStyleClass = new StringBuilder();
            if (component.isHideLabel()) {
                defaultStyleClass.append(Constants.COMPONENT_VALUE_HIDDEN);
            } else {
                defaultStyleClass.append(Constants.COMPONENT_VALUE_CLASS);
            }

            defaultStyleClass.append(" butter-component-checkbox pt-2");

            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", defaultStyleClass.toString(), null);

            if (!StringUtils.isEmpty(component.getDescription())) {
                writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
                writer.writeAttribute("class", "checkbox d-flex", null);
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
                writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
            }

            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }
}
