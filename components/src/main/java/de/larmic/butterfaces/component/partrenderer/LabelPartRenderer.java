package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

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
        if (!StringUtils.isEmpty(label) && !component.isHideLabel()) {
            final UIInput uiComponent = (UIInput) component;

            writer.startElement("label", uiComponent);
            if (!readonly) {
                writer.writeAttribute("for", uiComponent.getId(), null);
            }

            final String labelStyleClass = component.getLabelStyleClass();


            writer.writeAttribute("class", StringUtils.concatWithSpace(Constants.LABEL_STYLE_CLASS,
                    Constants.BOOTSTRAP_CONTROL_LABEL, Constants.TOOLTIP_LABEL_CLASS,
                    StringUtils.isEmpty(labelStyleClass) ? Constants.BOOTSTRAP_COL_SM_2 : labelStyleClass), null);

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
            writer.writeAttribute("class", Constants.REQUIRED_SPAN_CLASS, null);
            writer.writeText("*", null);
            writer.endElement("span");
        }
    }

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }
}
