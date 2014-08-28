package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class OuterComponentWrapperPartRenderer {

    private static final String FLOATING_STYLE_CLASS = "butterfaces-component-floating";
    private static final String NON_FLOATING_STYLE_CLASS = "butterfaces-component-non-floating";

    private static final String COMPONENT_STYLE_CLASS = "butterfaces-component";
    private static final String COMPONENT_ID_POSTFIX = "_outerComponentDiv";

    private static final String BOOTSTRAP_ERROR = "has-error";
    private static final String BOOTSTRAP_CONTAINER = "form-group";

    public void renderComponentBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final boolean floating = component.getFloating();
        final boolean valid = component.isValid();

        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", component.getClientId() + COMPONENT_ID_POSTFIX, null);

        final String floatingStyle = floating ? FLOATING_STYLE_CLASS : NON_FLOATING_STYLE_CLASS;
        final String validationClass = valid ? null : BOOTSTRAP_ERROR;
        final String styleClass = StringUtils.concatWithSpace(COMPONENT_STYLE_CLASS, BOOTSTRAP_CONTAINER,
                component.getComponentStyleClass(), validationClass, floatingStyle);

        writer.writeAttribute("class", styleClass, null);
    }

    public void renderComponentEnd(final ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }
}
