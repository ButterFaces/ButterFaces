package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class LabelPartRenderer {

    private static final String LABEL_MARKER_STYLE_CLASS = "larmic-component-label-marker";
    private static final String LABEL_STYLE_CLASS = "larmic-component-label";
    private static final String TOOLTIP_LABEL_CLASS = "larmic-component-label-tooltip";
    private static final String REQUIRED_SPAN_CLASS = "larmic-component-required";

    private static final String BOOTSTRAP_CONTROL_LABEL = "control-label";

    public void renderLabel(final HtmlInputComponent component, final ResponseWriter responseWriter) throws IOException {
        final boolean readonly = component.isReadonly();
        final boolean required = component.isRequired();
        final String label = component.getLabel();

        writeLabelIfNecessary(component, readonly, required, label, responseWriter);
    }

    private void writeLabelIfNecessary(final HtmlInputComponent component, final boolean readonly,
                                       final boolean required, final String label, final ResponseWriter writer) throws IOException {
        if (!StringUtils.isEmpty(label)) {
            final UIInput uiComponent = (UIInput) component;

            writer.startElement("label", uiComponent);
            if (!readonly) {
                writer.writeAttribute("for", uiComponent.getId(), null);
            }

            writer.writeAttribute("class", StringUtils.concatWithSpace(LABEL_STYLE_CLASS, BOOTSTRAP_CONTROL_LABEL,
                    LABEL_MARKER_STYLE_CLASS, TOOLTIP_LABEL_CLASS, component.getLabelStyleClass()), null);

            writer.startElement("abbr", uiComponent);
            if (this.isTooltipNecessary(component)) {
                writer.writeAttribute("title", component.getTooltip(), null);
            }
            writer.writeText(component.getLabel(), null);
            writer.endElement("abbr");

            this.writeRequiredSpanIfNecessary(component.getClientId(), readonly, required, writer);

            writer.endElement("label");
        }
    }

    private void writeRequiredSpanIfNecessary(final String clientId, final boolean readonly, final boolean required,
                                              final ResponseWriter writer) throws IOException {
        if (required && !readonly) {
            writer.startElement("span", null);
            writer.writeAttribute("id", clientId + "_requiredLabel", null);
            writer.writeAttribute("class", REQUIRED_SPAN_CLASS, null);
            writer.writeText("*", null);
            writer.endElement("span");
        }
    }

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }
}
