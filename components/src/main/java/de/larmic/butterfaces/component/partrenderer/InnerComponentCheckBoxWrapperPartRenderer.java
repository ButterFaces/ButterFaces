package de.larmic.butterfaces.component.partrenderer;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;

public class InnerComponentCheckBoxWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlInputComponent component, final ResponseWriter writer)
            throws IOException {
        final HtmlCheckBox uiComponent = (HtmlCheckBox) component;

        if (!component.isReadonly()) {
            final StringBuilder defaultStyleClass = new StringBuilder();
            if (component.isHideLabel()) {
                defaultStyleClass.append("butter-component-value-hiddenLabel");
            }else {
                defaultStyleClass.append("butter-component-value");
            }

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", defaultStyleClass.toString(), null);

            if (!StringUtils.isEmpty(uiComponent.getDescription())) {
                writer.startElement("div", uiComponent);
                writer.writeAttribute("class", "checkbox butter-component-checkbox-withDescription", null);
                writer.startElement("label", uiComponent);
            }
        }
    }

    public void renderInnerWrapperEnd(final HtmlInputComponent component, final ResponseWriter writer)
            throws IOException {
        if (!component.isReadonly()) {
            final HtmlCheckBox uiComponent = (HtmlCheckBox) component;

            if (!StringUtils.isEmpty(uiComponent.getDescription())) {
                writer.startElement("span", uiComponent);
                writer.writeAttribute("class", "butter-component-checkbox-description", null);
                writer.writeText(uiComponent.getDescription(), null);
                writer.endElement("span");
                writer.endElement("label");
                writer.endElement("div");
            }

            writer.startElement("script", uiComponent);
            writer.writeText("addLabelAttributeToInnerComponent('" + component.getClientId() + "', '"
                    + component.getLabel() + "');", null);
            writer.endElement("script");

            writer.endElement("div");
        }
    }
}
