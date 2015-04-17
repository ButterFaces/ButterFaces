package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 31.07.14.
 */
@FacesRenderer(componentFamily = HtmlTooltip.COMPONENT_FAMILY, rendererType = HtmlTooltip.RENDERER_TYPE)
public class TooltipRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTooltip tooltip = (HtmlTooltip) component;

        final ResponseWriter writer = context.getResponseWriter();

        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        final String contentId = component.getClientId().replace(separatorChar + "", "_");

        writer.startElement("span", tooltip);
        writer.writeAttribute("name", contentId, null);
        writer.writeAttribute("class", "butter-component-tooltip-content", null);
        for (UIComponent child : tooltip.getChildren()) {
            child.encodeAll(context);
        }
        writer.endElement("span");

        final String title = StringUtils.isNotEmpty(tooltip.getTitle()) ? tooltip.getTitle() : "";
        final String placement = StringUtils.isNotEmpty(tooltip.getPlacement()) ? tooltip.getPlacement() : "right";

        writer.startElement("script", tooltip);
        writer.writeText("jQuery('", null);
        writer.writeText(tooltip.getjQueryTargetSelector(), null);
        writer.writeText("').butterTooltip('", null);
        writer.writeText("hover", null);
        writer.writeText("', '", null);
        writer.writeText(title, null);
        writer.writeText("', '", null);
        writer.writeText(placement, null);
        writer.writeText("', '", null);
        writer.writeText(contentId, null);
        writer.writeText("');", null);
        writer.endElement("script");
    }
}
