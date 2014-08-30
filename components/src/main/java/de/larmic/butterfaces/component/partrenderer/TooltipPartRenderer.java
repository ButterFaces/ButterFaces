package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class TooltipPartRenderer {

    public void renderTooltip(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId() + Constants.OUTERDIV_POSTFIX;

        if (isTooltipNecessary(component)) {
            renderTooltipElement(component, writer, uiComponent);
            RenderUtils.renderJQueryPluginCall(outerComponentId, "butterTooltip()", writer, uiComponent);
        }
    }

    private void renderTooltipElement(HtmlInputComponent component, ResponseWriter writer, UIInput uiComponent) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip butter-component-tooltip-hidden", null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip-arrow", null);
        writer.endElement("div");
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip-text", null);
        writer.writeText(component.getTooltip(), null);
        writer.endElement("div");
        writer.endElement("div");
    }

    /*private String calculateShowTooltip(final HtmlInputComponent component) {
        final boolean tooltipNecessary = this.isTooltipNecessary(component);

        Boolean showTooltip = (tooltipNecessary || !component.isValid()) && !component.isReadonly();

        return showTooltip.toString();
    }*/

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }
}
