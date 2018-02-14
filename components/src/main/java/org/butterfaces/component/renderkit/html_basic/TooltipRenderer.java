/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.component.html.text.HtmlTags;
import org.butterfaces.component.html.text.HtmlTreeBox;
import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlTooltip.COMPONENT_FAMILY, rendererType = HtmlTooltip.RENDERER_TYPE)
public class TooltipRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
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
            writer.writeAttribute("class", "butter-component-tooltip-temp-content", null);
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
            writer.writeText(",\n      viewport: ", null);
            writer.writeText("'" + StringUtils.getNotNullValue(tooltip.getViewport(), "body") + "'", null);
            writer.writeText("\n   })\n});", null);

            if (StringUtils.isNotEmpty(tooltip.getOnShow())
                    || StringUtils.isNotEmpty(tooltip.getOnShown())
                    || StringUtils.isNotEmpty(tooltip.getOnHide())
                    || StringUtils.isNotEmpty(tooltip.getOnHidden())) {
                encodePopoverEvent(forSelector, tooltip.getOnShow(), "show.bs.popover", component, writer);
                encodePopoverEvent(forSelector, tooltip.getOnShown(), "shown.bs.popover", component, writer);
                encodePopoverEvent(forSelector, tooltip.getOnHide(), "hide.bs.popover", component, writer);
                encodePopoverEvent(forSelector, tooltip.getOnHidden(), "hidden.bs.popover", component, writer);
            }

            if (StringUtils.isEmpty(tooltip.getFor()) && isTooltipParentReadonly(tooltip)) {
                // in this case popover will be docked in readonly text to calculate correct position
                // hover event should triggered correctly
                writer.writeText("jQuery(document).ready(function() {", null);
                writer.writeText("\n    jQuery(" + createParentForElement(tooltip) + ").find('.butter-component-label').on('" + convertTooltipTriggerToJavaScriptEvent(tooltip.getTrigger()) + "', function(e) {", component, null);
                writer.writeText("\n        jQuery(" + createParentReadonlyForElement(tooltip) + ").trigger(e.type);", component, null);
                writer.writeText("\n    });", component, null);
                writer.writeText("\n});\n", component, null);
            }

            writer.endElement("script");
        }
    }

    private String convertTooltipTriggerToJavaScriptEvent(final String trigger) {
        if ("click".equalsIgnoreCase(trigger)) {
            return "click";
        } else if ("focus".equalsIgnoreCase(trigger)) {
            return "focus";
        } else if ("manual".equalsIgnoreCase(trigger)) {
            // in case of manual no tooltip is supported on label event
            return "";
        }

        // default is hover
        return "mouseenter mouseleave";
    }

    private void encodePopoverEvent(final String forSelector,
                                    final String function,
                                    final String event,
                                    final UIComponent component,
                                    final ResponseWriter writer) throws IOException {
        if (StringUtils.isNotEmpty(function)) {
            writer.writeText("jQuery(document).ready(function() {\n", null);
            writer.writeText("    jQuery(" + forSelector + ").on('" + event + "', function() {\n ", component, null);
            writer.writeText("        " + function + ";\n", component, null);
            writer.writeText("    });\n", component, null);
            writer.writeText("});\n", component, null);
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

                writer.startElement(ELEMENT_DIV, component);
                writer.writeAttribute("class", "butter-component-tooltip-validation-error", null);
                writer.startElement("ul", component);

                while (componentMessages.hasNext()) {
                    writer.startElement("li", component);
                    writer.writeText(componentMessages.next().getDetail(), null);
                    writer.endElement("li");
                }

                writer.endElement("ul");
                writer.endElement(ELEMENT_DIV);
            }
        }
    }

    private String getForElement(final HtmlTooltip tooltip) {
        if (StringUtils.isNotEmpty(tooltip.getFor())) {
            return "'" + tooltip.getFor() + "'";
        } else if (tooltip.getParent() instanceof Tooltip) {
            final UIComponent parent = tooltip.getParent();

            if (parent instanceof Readonly && !(parent instanceof HtmlTags) && !(parent instanceof HtmlTreeBox) && ((Readonly) parent).isReadonly()) {
                return createParentReadonlyForElement(tooltip);
            }

            return createParentForElement(tooltip);
        }

        return null;
    }

    private String createParentReadonlyForElement(final HtmlTooltip tooltip) {
        return createParentForElement(tooltip) + ").find('.butter-component-value-readonly-wrapper'";
    }

    private String createParentForElement(final HtmlTooltip tooltip) {
        return "document.getElementById('" + tooltip.getParent().getClientId() + "')";
    }

    private boolean isTooltipParentReadonly(final HtmlTooltip tooltip) {
        return tooltip.getParent() instanceof Tooltip
                && !(tooltip.getParent() instanceof HtmlTags)
                && tooltip.getParent() instanceof Readonly
                && ((Readonly) tooltip.getParent()).isReadonly();
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
