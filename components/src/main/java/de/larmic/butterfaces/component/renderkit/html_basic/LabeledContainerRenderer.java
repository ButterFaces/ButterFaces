/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlLabeledContainer;
import de.larmic.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.LabelPartRenderer;
import de.larmic.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlLabeledContainer.COMPONENT_FAMILY, rendererType = HtmlLabeledContainer.RENDERER_TYPE)
public class LabeledContainerRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer, "butter-labeled-container");

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(writer, false);

        // Close outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }
}
