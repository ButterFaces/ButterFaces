/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.feature.Readonly;
import de.larmic.butterfaces.component.html.feature.Style;
import de.larmic.butterfaces.component.html.feature.StyleClass;
import de.larmic.butterfaces.component.html.feature.Validation;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public class OuterComponentWrapperPartRenderer {

    public void renderComponentBegin(final UIComponent component, final ResponseWriter writer) throws IOException {
        this.renderComponentBegin(component, writer, "");
    }

    public void renderComponentBegin(final UIComponent component, final ResponseWriter writer, final String addtionalStyleClass) throws IOException {
        final String validationClass = component instanceof Validation && !((Validation) component).isValid() ? Constants.BOOTSTRAP_ERROR : "";
        final String componentStyleClass = component instanceof StyleClass ? ((StyleClass) component).getStyleClass() : "";
        final String componentStyle = component instanceof Style ? ((Style) component).getStyle() : "";
        final String readonlyClass = component instanceof Readonly && ((Readonly) component).isReadonly() ? Constants.COMPONENT_READONLY_STYLE_CLASS : "";

        writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
        writer.writeAttribute("id", component.getClientId(), null);

        final String styleClass =
                StringUtils.concatWithSpace(
                        Constants.COMPONENT_STYLE_CLASS,
                        Constants.BOOTSTRAP_CONTAINER,
                        componentStyleClass,
                        validationClass,
                        readonlyClass,
                        addtionalStyleClass
                );

        writer.writeAttribute("class", styleClass, null);

        if (StringUtils.isNotEmpty(componentStyle)) {
            writer.writeAttribute("style", componentStyle, null);
        }
    }

    public void renderComponentEnd(final ResponseWriter writer) throws IOException {
        writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
    }
}
