/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.partrenderer;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.feature.HideLabel;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Readonly;
import org.butterfaces.component.html.feature.Required;
import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

/**
 * @author Lars Michaelis
 */
public class LabelPartRenderer {

    public void renderLabel(final UIComponent component, final ResponseWriter responseWriter) throws IOException {
        this.renderLabel(component, responseWriter, component.getClientId());
    }

    public void renderLabel(final UIComponent component, final ResponseWriter responseWriter, final String clientId) throws IOException {
        final boolean readonly = component instanceof Readonly && ((Readonly) component).isReadonly();
        final boolean required = component instanceof Required && ((Required) component).isRequired();
        final String label = component instanceof Label ? ((Label) component).getLabel() : "";

        writeLabelIfNecessary(component, readonly, required, label, responseWriter, clientId);
    }

    private void writeLabelIfNecessary(final UIComponent component,
                                       final boolean readonly,
                                       final boolean required,
                                       final String label,
                                       final ResponseWriter writer,
                                       final String clientId) throws IOException {
        final boolean hideLabel = component instanceof HideLabel && ((HideLabel) component).isHideLabel();

        if (!hideLabel) {
            writer.startElement("label", component);
            if (!readonly) {
                writer.writeAttribute("for", clientId, null);
            }

            writer.writeAttribute("class", StringUtils.concatWithSpace(
                    Constants.LABEL_STYLE_CLASS,
                    shouldRenderTooltip(component) ? Constants.TOOLTIP_LABEL_CLASS : ""), null);

            if (!StringUtils.isEmpty(label)) {
                writer.startElement("abbr", component);
                writer.startElement("span", component);
                writer.writeText(label, null);
                writer.endElement("span");
                this.writeRequiredSpanIfNecessary(clientId, readonly, required, writer);
                writer.endElement("abbr");
            }


            writer.endElement("label");
        }
    }

    private boolean shouldRenderTooltip(UIComponent component) {
        if (component instanceof HtmlInputComponent && !((HtmlInputComponent) component).isValid()) {
            return true;
        }

        for (UIComponent uiComponent : component.getChildren()) {
            if (uiComponent instanceof HtmlTooltip) {
                if (uiComponent.isRendered()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void writeRequiredSpanIfNecessary(final String clientId, final boolean readonly, final boolean required,
                                              final ResponseWriter writer) throws IOException {
        if (required && !readonly) {
            final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
            writer.startElement("span", null);
            writer.writeAttribute("id", clientId + separatorChar + "requiredLabel", null);
            writer.writeAttribute("class", Constants.REQUIRED_SPAN_CLASS, null);
            writer.endElement("span");
        }
    }
}
