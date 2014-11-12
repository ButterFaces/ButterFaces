package de.larmic.butterfaces.component.renderkit.html_basic.ajax;

import de.larmic.butterfaces.component.html.ajax.WaitingPanel;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.HtmlBasicRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 31.07.14.
 */
@FacesRenderer(componentFamily = WaitingPanel.COMPONENT_FAMILY, rendererType = WaitingPanel.RENDERER_TYPE)
public class WaitingPanelRenderer extends HtmlBasicRenderer {

    public static final int DEFAULT_WAITING_PANEL_DELAY = 500;

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final WaitingPanel waitingPanel = (WaitingPanel) component;

        final String style = waitingPanel.getStyle();
        final String styleClass = waitingPanel.getStyleClass();

        writer.startElement(ELEMENT_DIV, component);

        this.writeIdAttribute(context, writer, component);

        if (StringUtils.isNotEmpty(style)) {
            writer.writeAttribute("style", style, null);
        }

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", "butter-component-waitingPanel butter-component-waitingPanel-hide " + styleClass, null);
        } else {
            writer.writeAttribute("class", "butter-component-waitingPanel butter-component-waitingPanel-hide", null);
        }

        writer.startElement(ELEMENT_DIV, component);
        writer.writeAttribute("class", "butter-component-waitingPanel-body", null);
        writer.writeText("Processing", component, null);
        writer.startElement(ELEMENT_SPAN, component);
        writer.writeAttribute("class", "butter-component-waitingPanel-processing", null);
        writer.endElement(ELEMENT_SPAN);
        writer.endElement(ELEMENT_DIV);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final WaitingPanel waitingPanel = (WaitingPanel) component;

        writer.endElement(ELEMENT_DIV);

        final String pluginFunctionCall = "waitingPanel(" + createButterTreeJQueryParameter(waitingPanel) + ")";
        RenderUtils.renderJQueryPluginCall(component.getClientId(), pluginFunctionCall, writer, component);
    }

    private String createButterTreeJQueryParameter(final WaitingPanel waitingPanel) {
        final int waitingPanelDelay = getWaitingPanelDelay(waitingPanel);

        return "{waitingPanelDelay: '" + waitingPanelDelay + "'}";
    }

    private int getWaitingPanelDelay(final WaitingPanel waitingPanel) {
        if (waitingPanel.getDelay() != null) {
            return waitingPanel.getDelay() > 0 ? waitingPanel.getDelay() : 0;
        }

        return DEFAULT_WAITING_PANEL_DELAY;
    }
}
