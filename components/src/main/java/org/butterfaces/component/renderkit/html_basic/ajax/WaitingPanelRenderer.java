package org.butterfaces.component.renderkit.html_basic.ajax;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.ajax.HtmlWaitingPanel;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 31.07.14.
 */
@FacesRenderer(componentFamily = HtmlWaitingPanel.COMPONENT_FAMILY, rendererType = HtmlWaitingPanel.RENDERER_TYPE)
public class WaitingPanelRenderer extends HtmlBasicRenderer {

    public static final int DEFAULT_WAITING_PANEL_DELAY = 500;

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlWaitingPanel waitingPanel = (HtmlWaitingPanel) component;

        final String style = waitingPanel.getStyle();
        final String styleClass = waitingPanel.getStyleClass();

        writer.startElement(ELEMENT_DIV, component);

        this.writeIdAttribute(context, writer, component);

        if (StringUtils.isNotEmpty(style)) {
            writer.writeAttribute("style", style, null);
        }

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", "butter-component-waitingPanel " + styleClass, null);
        } else {
            writer.writeAttribute("class", "butter-component-waitingPanel", null);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlWaitingPanel waitingPanel = (HtmlWaitingPanel) component;

        writer.endElement(ELEMENT_DIV); // component div

        final String pluginFunctionCall = "waitingPanel(" + createButterTreeJQueryParameter(waitingPanel) + ")";
        RenderUtils.renderJQueryPluginCall(component.getClientId(), pluginFunctionCall, writer, component);
    }

    private String createButterTreeJQueryParameter(final HtmlWaitingPanel waitingPanel) {
        final int waitingPanelDelay = getWaitingPanelDelay(waitingPanel);

        return "{waitingPanelDelay: '" + waitingPanelDelay + "', blockpage: " + waitingPanel.isBlockpage() + "}";
    }

    private int getWaitingPanelDelay(final HtmlWaitingPanel waitingPanel) {
        if (waitingPanel.getDelay() != null) {
            return waitingPanel.getDelay() > 0 ? waitingPanel.getDelay() : 0;
        }

        return DEFAULT_WAITING_PANEL_DELAY;
    }
}
