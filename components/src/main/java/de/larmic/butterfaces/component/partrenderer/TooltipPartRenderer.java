package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by larmic on 27.08.14.
 */
public class TooltipPartRenderer {

    public void renderTooltip(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId() + Constants.OUTERDIV_POSTFIX;

        if (calculateShowTooltip(component)) {
            renderTooltipElement(component, writer, uiComponent);
            RenderUtils.renderJQueryPluginCall(outerComponentId, "butterTooltip()", writer, uiComponent);
        }
    }

    private void renderTooltipElement(final HtmlInputComponent component, final ResponseWriter writer, final UIInput uiComponent) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip butter-component-tooltip-hidden", null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip-arrow", null);
        writer.endElement("div");
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "butter-component-tooltip-text", null);
        if (!StringUtils.isEmpty(component.getTooltip())) {
            writer.writeText(component.getTooltip(), null);
            renderValidationMessages(uiComponent, writer, true);
        } else {
            renderValidationMessages(uiComponent, writer, false);
        }
        writer.endElement("div");


        writer.endElement("div");
    }

    private void renderValidationMessages(final UIInput uiComponent, final ResponseWriter writer, final boolean tooltipExists) throws IOException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();

        if (clientIdsWithMessages.hasNext() && tooltipExists) {
            writer.startElement("hr", uiComponent);
            writer.endElement("hr");
        }

        while (clientIdsWithMessages.hasNext()) {
            final String clientIdWithMessages = clientIdsWithMessages.next();
            if (uiComponent.getClientId().equals(clientIdWithMessages)) {
                final Iterator<FacesMessage> componentMessages = context.getMessages(clientIdWithMessages);

                writer.startElement("div", uiComponent);
                writer.writeAttribute("class", "butter-component-tooltip-validation-error", null);
                writer.startElement("ul", uiComponent);

                while (componentMessages.hasNext()) {
                    writer.startElement("li", uiComponent);
                    writer.writeText(componentMessages.next().getDetail(), null);
                    writer.endElement("li");
                }

                writer.endElement("ul");
                writer.endElement("div");
            }
        }
    }

    private boolean calculateShowTooltip(final HtmlInputComponent component) {
        final boolean tooltipNecessary = this.isTooltipNecessary(component);
        return (tooltipNecessary || !component.isValid()) && !component.isReadonly();
    }

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }
}
