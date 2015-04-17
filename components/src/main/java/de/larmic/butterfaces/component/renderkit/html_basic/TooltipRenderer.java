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
        final String forSelector = tooltip.getFor();

        writer.startElement("span", tooltip);
        writer.writeAttribute("name", contentId, null);
        writer.writeAttribute("class", "butter-component-tooltip-content", null);
        for (UIComponent child : tooltip.getChildren()) {
            child.encodeAll(context);
        }
        writer.endElement("span");

        writer.startElement("script", tooltip);
        writer.writeText("jQuery(document).ready(function() {\n", null);
        writer.writeText("   jQuery('", null);
        writer.writeText(forSelector, null);
        writer.writeText("')._butterTooltip({\n", null);
        writer.writeText("      trigger: ", null);
        writer.writeText(getNullSafeStringParameter(tooltip.getTrigger()), null);
        writer.writeText(",\n      title: ", null);
        writer.writeText(getNullSafeStringParameter(tooltip.getTitle()), null);
        writer.writeText(",\n      placement: ", null);
        writer.writeText(getNullSafeStringParameter(tooltip.getPlacement()), null);
        writer.writeText(",\n      placementFunction: ", null);
        writer.writeText(getNullSafeFunctionParameter(tooltip.getPlacementFunction()), null);
        writer.writeText(",\n      contentByName: '", null);
        writer.writeText(contentId, null);
        writer.writeText("'\n   })\n});", null);
        writer.endElement("script");
    }

    private String getNullSafeStringParameter(final String value) {
        return "'" + StringUtils.getNullSafeValue(value) + "'";
    }

    private String getNullSafeFunctionParameter(final String value) {
        final String nullSafeValue = StringUtils.getNullSafeValue(value);
        return StringUtils.isNotEmpty(nullSafeValue) ? nullSafeValue : "''";
    }
}
