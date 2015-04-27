package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.html.feature.Tooltip;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class LabelPartRenderer {


    public void renderLabel(final HtmlInputComponent component, final ResponseWriter responseWriter) throws IOException {
        final boolean readonly = component.isReadonly();
        final boolean required = component.isRequired();
        final String label = component.getLabel();

        writeLabelIfNecessary(component, readonly, required, label, responseWriter);
    }

    private void writeLabelIfNecessary(final HtmlInputComponent component, final boolean readonly,
                                       final boolean required, final String label, final ResponseWriter writer) throws IOException {
        if (!component.isHideLabel()) {
            final UIInput uiComponent = (UIInput) component;

            writer.startElement("label", uiComponent);
            if (!readonly) {
                writer.writeAttribute("for", uiComponent.getId(), null);
            }

            writer.writeAttribute("class", StringUtils.concatWithSpace(Constants.LABEL_STYLE_CLASS,
                    Constants.BOOTSTRAP_CONTROL_LABEL, Constants.TOOLTIP_LABEL_CLASS), null);

            if (!StringUtils.isEmpty(label)) {
                writer.startElement("abbr", uiComponent);
                if (this.isTooltipNecessary(uiComponent)) {
                    writer.writeAttribute("title", "", null);
                }
                writer.writeText(component.getLabel(), null);
                writer.endElement("abbr");
            }

            this.writeRequiredSpanIfNecessary(component.getClientId(), readonly, required, writer);

            writer.endElement("label");
        }
    }

    private void writeRequiredSpanIfNecessary(final String clientId, final boolean readonly, final boolean required,
                                              final ResponseWriter writer) throws IOException {
        if (required && !readonly) {
            writer.startElement("span", null);
            writer.writeAttribute("id", clientId + "_requiredLabel", null);
            writer.writeAttribute("class", Constants.REQUIRED_SPAN_CLASS, null);
            writer.writeText("*", null);
            writer.endElement("span");
        }
    }

    private boolean isTooltipNecessary(final javax.faces.component.UIComponentBase component) {
        if (component instanceof Tooltip) {
            for (UIComponent uiComponent : component.getChildren()) {
                if (uiComponent instanceof HtmlTooltip) {
                    return true;
                }
            }
        }

        return false;
    }
}
