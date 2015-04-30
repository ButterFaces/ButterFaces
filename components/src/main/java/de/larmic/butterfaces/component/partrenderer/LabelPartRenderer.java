package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.feature.HideLabel;
import de.larmic.butterfaces.component.html.feature.Label;
import de.larmic.butterfaces.component.html.feature.Readonly;
import de.larmic.butterfaces.component.html.feature.Required;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class LabelPartRenderer {

    public void renderLabel(final UIComponent component, final ResponseWriter responseWriter) throws IOException {
        final boolean readonly = component instanceof Readonly && ((Readonly) component).isReadonly();
        final boolean required = component instanceof Required && ((Required) component).isRequired();
        final String label = component instanceof Label ? ((Label) component).getLabel() : "";

        writeLabelIfNecessary(component, readonly, required, label, responseWriter);
    }

    private void writeLabelIfNecessary(final UIComponent component, final boolean readonly,
                                       final boolean required, final String label, final ResponseWriter writer) throws IOException {
        final boolean hideLabel = component instanceof HideLabel && ((HideLabel) component).isHideLabel();

        if (!hideLabel) {
            writer.startElement("label", component);
            if (!readonly) {
                writer.writeAttribute("for", component.getId(), null);
            }

            writer.writeAttribute("class", StringUtils.concatWithSpace(Constants.LABEL_STYLE_CLASS,
                    Constants.BOOTSTRAP_CONTROL_LABEL, Constants.TOOLTIP_LABEL_CLASS), null);

            if (!StringUtils.isEmpty(label)) {
                writer.startElement("abbr", component);
                writer.writeText(label, null);
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
}
