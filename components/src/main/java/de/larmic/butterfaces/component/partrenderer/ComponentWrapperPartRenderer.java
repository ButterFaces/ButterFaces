package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class ComponentWrapperPartRenderer {

    private static final String FLOATING_STYLE_CLASS = "larmic-component-floating";
    private static final String NON_FLOATING_STYLE_CLASS = "larmic-component-non-floating";

    private static final String COMPONENT_MARKER_STYLE_CLASS = "larmic-component-marker";
    private static final String COMPONENT_STYLE_CLASS = "larmic-component";
    private static final String COMPONENT_INVALID_STYLE_CLASS = "larmic-component-invalid";
    private static final String OUTERDIV_POSTFIX = "_outerComponentDiv";

    public void renderComponentBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final boolean disableDefaultStyleClasses = component.getDisableDefaultStyleClasses();
        final boolean floating = component.getFloating();
        final boolean valid = component.isValid();

        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", component.getClientId() + OUTERDIV_POSTFIX, null);

        final String floatingStyle = floating ? FLOATING_STYLE_CLASS : NON_FLOATING_STYLE_CLASS;
        final String validationClass = valid ? COMPONENT_INVALID_STYLE_CLASS : null;
        final String componentClass = disableDefaultStyleClasses ? null : COMPONENT_STYLE_CLASS;
        final String styleClass = StringUtils.concatStyles(COMPONENT_MARKER_STYLE_CLASS,
                componentClass, component.getComponentStyleClass(), validationClass, floatingStyle);

        writer.writeAttribute("class", styleClass, null);
    }

    public void renderComponentEnd(final ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }
}
