package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class InnterComponentWrapperPartRenderer {

    private static final String INPUT_COMPONENT_MARKER = "butterfaces-input-component";
    private static final String INVALID_STYLE_CLASS = "butterfaces-invalid";

    private static final String BOOTSTRAP_FORM_CONTROL = "form-control";

    public void renderInnerWrapperBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        if (!component.isReadonly()) {
            final String styleClass = StringUtils.concatWithSpace(INPUT_COMPONENT_MARKER, BOOTSTRAP_FORM_CONTROL,
                    component.getInputStyleClass(), !uiComponent.isValid() ? INVALID_STYLE_CLASS : null);

            uiComponent.getAttributes().put("styleClass", styleClass);

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", "col-sm-10", null);
        }
    }

    public void renderInnerWrapperEnd(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        if (!component.isReadonly()) {
            writer.endElement("div");
        }
    }
}
