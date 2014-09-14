package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class MaxLengthPartRenderer {

    public void renderMaxLength(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId() + Constants.OUTERDIV_POSTFIX;

        if (isMaxLengthNecessary(component)) {
            renderMaxLengthElement(writer, uiComponent);
            RenderUtils.renderJQueryPluginCall(outerComponentId, "butterMaxLength()", writer, uiComponent);
        }
    }

    private void renderMaxLengthElement(final ResponseWriter writer, final UIInput uiComponent) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", Constants.TEXT_AREA_MAXLENGTH_COUNTER_CLASS, null);
        writer.endElement("div");
    }


    private boolean isMaxLengthNecessary(final HtmlInputComponent component) {
        return ((HtmlTextArea) component).getMaxLength() != null;
    }
}
