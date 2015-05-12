package de.larmic.butterfaces.component.partrenderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import de.larmic.butterfaces.component.html.feature.Readonly;
import de.larmic.butterfaces.component.html.feature.StyleClass;
import de.larmic.butterfaces.component.html.feature.Validation;

/**
 * Created by larmic on 27.08.14.
 */
public class OuterComponentWrapperPartRenderer {

    public void renderComponentBegin(final UIComponent component, final ResponseWriter writer) throws IOException {
        this.renderComponentBegin(component, writer, "");
    }

    public void renderComponentBegin(final UIComponent component, final ResponseWriter writer, final String addtionalStyleClass) throws IOException {
        final String validationClass = component instanceof Validation && !((Validation) component).isValid() ? Constants.BOOTSTRAP_ERROR : "";
        final String componentStyleClass = component instanceof StyleClass ? ((StyleClass) component).getStyleClass() : "";
        final String readonlyClass = component instanceof Readonly && ((Readonly) component).isReadonly() ? Constants.COMPONENT_READONLY_STYLE_CLASS : "";

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), null);

        final String styleClass =
                StringUtils.concatWithSpace(
                        Constants.COMPONENT_STYLE_CLASS,
                        Constants.BOOTSTRAP_CONTAINER,
                        componentStyleClass,
                        validationClass,
                        readonlyClass,
                        addtionalStyleClass
                );

        writer.writeAttribute("class", styleClass, null);
    }

    public void renderComponentEnd(final ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }
}
