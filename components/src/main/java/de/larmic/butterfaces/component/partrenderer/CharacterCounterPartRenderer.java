package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class CharacterCounterPartRenderer {

    public void renderCharacterCounter(final HtmlInputComponent component, final ResponseWriter responseWriter) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final String outerComponentId = component.getClientId() + Constants.OUTERDIV_POSTFIX;

        if (uiComponent instanceof HtmlTextArea) {
            final StringBuffer jsCall = new StringBuffer();
            jsCall.append("new TextareaComponentHandler");
            jsCall.append("('").append(outerComponentId).append("', {");
            jsCall.append("showTooltip:" + this.calculateShowTooltip(component));

            if (((HtmlTextArea) uiComponent).getMaxLength() != null) {
                responseWriter.startElement("div", uiComponent);
                responseWriter.writeAttribute("class", Constants.TEXT_AREA_MAXLENGTH_COUNTER_CLASS, null);
                responseWriter.endElement("div");

                jsCall.append(", maxLength:").append(((HtmlTextArea) uiComponent).getMaxLength().intValue());
            }


            jsCall.append("});");

            responseWriter.startElement("script", uiComponent);
            responseWriter.writeText(jsCall.toString(), null);
            responseWriter.endElement("script");
        }
    }

    private String calculateShowTooltip(final HtmlInputComponent component) {
        final boolean tooltipNecessary = this.isTooltipNecessary(component);

        Boolean showTooltip = (tooltipNecessary || !component.isValid()) && !component.isReadonly();

        return showTooltip.toString();
    }

    private boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !StringUtils.isEmpty(component.getTooltip());
    }
}
