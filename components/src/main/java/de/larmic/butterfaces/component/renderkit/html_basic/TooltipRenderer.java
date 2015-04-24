package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.html.feature.Tooltip;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Iterator;

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
        final String forSelector = getForElement(tooltip);

        if (StringUtils.isNotEmpty(forSelector)) {
            writer.startElement("span", tooltip);
            writer.writeAttribute("name", contentId, null);
            writer.writeAttribute("class", "butter-component-tooltip-content", null);
            if (StringUtils.isEmpty(tooltip.getFor()) && tooltip.getParent() instanceof Tooltip) {
                renderValidationErrors(context, writer, tooltip.getParent());
            }
            for (UIComponent child : tooltip.getChildren()) {
                child.encodeAll(context);
            }
            writer.endElement("span");

            writer.startElement("script", tooltip);
            writer.writeText("jQuery(document).ready(function() {\n", null);
            writer.writeText("   jQuery(", null);
            writer.writeText(forSelector, null);
            writer.writeText(")._butterTooltip({\n", null);
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
            writer.writeText("',\n      minVerticalOffset: ", null);
            writer.writeText(getNullSafeIntegerParameter(tooltip.getMinVerticalOffset()), null);
            writer.writeText(",\n      minHorizontalOffset: ", null);
            writer.writeText(getNullSafeIntegerParameter(tooltip.getMinHorizontalOffset()), null);
            writer.writeText("\n   })\n});", null);
            writer.endElement("script");
        }
    }

    private void renderValidationErrors(final FacesContext context,
                                        final ResponseWriter writer,
                                        final UIComponent component) throws IOException {
        final Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();

        while (clientIdsWithMessages.hasNext()) {
            final String clientIdWithMessages = clientIdsWithMessages.next();
            if (component.getClientId().equals(clientIdWithMessages)) {
                final Iterator<FacesMessage> componentMessages = context.getMessages(clientIdWithMessages);

                writer.startElement("div", component);
                writer.writeAttribute("class", "butter-component-tooltip-validation-error", null);
                writer.startElement("ul", component);

                while (componentMessages.hasNext()) {
                    writer.startElement("li", component);
                    writer.writeText(componentMessages.next().getDetail(), null);
                    writer.endElement("li");
                }

                writer.endElement("ul");
                writer.endElement("div");
            }
        }
    }

    private String getForElement(HtmlTooltip tooltip) {
        if (StringUtils.isNotEmpty(tooltip.getFor())) {
            return "'" + tooltip.getFor() + "'";
        } else if (tooltip.getParent() instanceof Tooltip) {
            return "document.getElementById('" + tooltip.getParent().getClientId() + "')";
        }

        return null;
    }

    private String getNullSafeStringParameter(final String value) {
        return "'" + StringUtils.getNullSafeValue(value) + "'";
    }

    private String getNullSafeFunctionParameter(final String value) {
        final String nullSafeValue = StringUtils.getNullSafeValue(value);
        return StringUtils.isNotEmpty(nullSafeValue) ? nullSafeValue : "''";
    }

    private String getNullSafeIntegerParameter(final Integer value) {
        return value == null ? "''" : String.valueOf(value);
    }
}
