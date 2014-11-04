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

        writer.startElement("div", component);

        this.writeIdAttribute(context, writer, component);

        if (StringUtils.isNotEmpty(style)) {
            writer.writeAttribute("style", "display:none" + style, null);
        } else {
            writer.writeAttribute("style", "display:none", null);
        }

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", "butter-component-waitingPanel " + styleClass, null);
        } else {
            writer.writeAttribute("class", "butter-component-waitingPanel", null);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "waitingPanel()", writer, component);
    }

}
