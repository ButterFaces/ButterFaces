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

    private static final String TOOLTIP_CLASS = "larmic-component-tooltip";
    private static final String ERROR_MESSAGE_CLASS = "larmic-component-error-message";
    private static final String TOOLTIP_DIV_CLIENT_ID_POSTFIX = "_tooltip";
    private static final String OUTERDIV_POSTFIX = "_outerComponentDiv";

    public void renderTooltip(final HtmlInputComponent component, final boolean renderJavaScript, final ResponseWriter responseWriter, final FacesContext context) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final boolean tooltipNecessary = this.isTooltipNecessary(component);

        if ((tooltipNecessary || !component.isValid()) && !component.isReadonly()) {
            responseWriter.startElement("div", uiComponent);
            responseWriter.writeAttribute("id", uiComponent.getClientId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX, null);
            responseWriter.writeAttribute("class", TOOLTIP_CLASS, null);

            responseWriter.startElement("div", uiComponent);
            responseWriter.writeAttribute("class", "noteConnector", null);
            responseWriter.endElement("div");

            if (tooltipNecessary) {
                responseWriter.startElement("div", uiComponent);
                responseWriter.writeAttribute("class", "noteContent", null);
                responseWriter.writeText(component.getTooltip(), null);
                responseWriter.endElement("div");
            }

            final Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();

            while (clientIdsWithMessages.hasNext()) {
                final String clientIdWithMessages = clientIdsWithMessages.next();
                if (uiComponent.getClientId().equals(clientIdWithMessages)) {
                    final Iterator<FacesMessage> componentMessages = context.getMessages(clientIdWithMessages);

                    responseWriter.startElement("div", uiComponent);
                    responseWriter.writeAttribute("class", ERROR_MESSAGE_CLASS, null);
                    responseWriter.startElement("ul", uiComponent);

                    while (componentMessages.hasNext()) {
                        responseWriter.startElement("li", uiComponent);
                        responseWriter.writeText(componentMessages.next().getDetail(), null);
                        responseWriter.endElement("li");
                    }

                    responseWriter.endElement("ul");
                    responseWriter.endElement("div");
                }
            }

            responseWriter.endElement("div");
        }

        if (renderJavaScript) {
            renderTooltipJavaScript(component, responseWriter);
        }
    }

    private void renderTooltipJavaScript(final HtmlInputComponent component, final ResponseWriter responseWriter) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId() + OUTERDIV_POSTFIX;

        final StringBuffer jsCall = new StringBuffer();
        jsCall.append("new ComponentHandler");
        jsCall.append("('").append(outerComponentId).append("', {");
        jsCall.append("showTooltip:" + calculateShowTooltip(component));
        jsCall.append("});");

        responseWriter.startElement("script", uiComponent);
        responseWriter.writeText(jsCall.toString(), null);
        responseWriter.endElement("script");
    }

    public String calculateShowTooltip(final HtmlInputComponent component) {
        final boolean tooltipNecessary = this.isTooltipNecessary(component);

        Boolean showTooltip = (tooltipNecessary || !component.isValid()) && !component.isReadonly();

        return showTooltip.toString();
    }

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }

}
