package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class InnerComponentCheckBoxWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlInputComponent component, final ResponseWriter writer)
            throws IOException {
        final HtmlCheckBox uiComponent = (HtmlCheckBox) component;

        if (!component.isReadonly()) {
            final StringBuilder defaultStyleClass = new StringBuilder();
            if (component.isHideLabel()) {
                defaultStyleClass.append("butter-component-value-hiddenLabel");
            } else {
                defaultStyleClass.append("butter-component-value");
            }

            defaultStyleClass.append(" butter-component-checkbox");

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", defaultStyleClass.toString(), null);

            if (!StringUtils.isEmpty(uiComponent.getDescription())) {
                writer.startElement("div", uiComponent);
                writer.writeAttribute("class", "checkbox withDescription", null);
                writer.startElement("label", uiComponent);
            } else {

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

            final Set<String> eventNames = ((UIInput) component).getClientBehaviors().keySet();
            final Iterator<String> eventNamesIterator = eventNames.iterator();

            if (eventNamesIterator.hasNext()) {
                final StringBuilder sb = new StringBuilder("[");

                while (eventNamesIterator.hasNext()) {
                    sb.append("'");
                    sb.append(eventNamesIterator.next());
                    sb.append("'");

                    if (eventNamesIterator.hasNext()) {
                        sb.append(", ");
                    }
                }

                sb.append("]");

                final String function = "butter.fix.updateMojarraScriptSourceId('" + component.getClientId() + "', " + sb.toString() + ");";
                RenderUtils.renderJavaScriptCall(function, writer, uiComponent);
            }

            writer.endElement("div");
        }
    }
}
