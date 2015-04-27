package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class OuterComponentWrapperPartRenderer {

    public void renderComponentBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final boolean valid = component.isValid();

        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", component.getClientId(), null);

        final String readonly = component.isReadonly() ? "butter-component-readonly" : "";
        final String validationClass = valid ? null : Constants.BOOTSTRAP_ERROR;
        final String styleClass = StringUtils.concatWithSpace(Constants.COMPONENT_STYLE_CLASS, Constants.BOOTSTRAP_CONTAINER,
                component.getStyleClass(), validationClass, readonly);

        writer.writeAttribute("class", styleClass, null);
    }

    public void renderComponentEnd(final ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }
}
