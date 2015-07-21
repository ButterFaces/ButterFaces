package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class MaxLengthPartRenderer {

    public void renderMaxLength(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId();

        if (isMaxLengthNecessary(component)) {
            renderMaxLengthElement(writer, uiComponent);
            RenderUtils.renderJQueryPluginCall(outerComponentId, "butterMaxLength({maxLength:"
                    + ((HtmlTextArea) component).getMaxLength() + "})", writer, uiComponent);
        }
    }

    private void renderMaxLengthElement(final ResponseWriter writer, final UIInput uiComponent) throws IOException {
        writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, uiComponent);
        writer.writeAttribute("class", Constants.TEXT_AREA_MAXLENGTH_COUNTER_CLASS, null);
        writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
    }


    private boolean isMaxLengthNecessary(final HtmlInputComponent component) {
        return ((HtmlTextArea) component).getMaxLength() != null;
    }
}
