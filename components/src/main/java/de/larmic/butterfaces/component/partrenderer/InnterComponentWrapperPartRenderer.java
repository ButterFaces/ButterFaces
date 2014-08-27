package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class InnterComponentWrapperPartRenderer {

    private static final String INPUT_CONTAINER_MARKER_STYLE_CLASS = "larmic-input-container-marker";
    private static final String INPUT_CONTAINER_STYLE_CLASS = "larmic-input-container";
    private static final String INPUT_COMPONENT_MARKER = "larmic-input-component-marker";
    private static final String INVALID_STYLE_CLASS = "larmic-component-input-invalid";

    public void renderInnerWrapperBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        this.initInputComponent(uiComponent);

        writer.startElement("div", uiComponent);
        final boolean disableDefaultStyleClasses = component.getDisableDefaultStyleClasses();
        final String inputContainerStyleClass = disableDefaultStyleClasses ? null : INPUT_CONTAINER_STYLE_CLASS;
        writer.writeAttribute("class", StringUtils.concatStyles(INPUT_CONTAINER_MARKER_STYLE_CLASS, inputContainerStyleClass), null);
    }

    public void renderInnerWrapperEnd(final ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }

    protected void initInputComponent(final UIInput component) {
        final HtmlInputComponent htmlInputComponent = (HtmlInputComponent) component;
        final String styleClass = StringUtils.concatStyles(INPUT_COMPONENT_MARKER,
                htmlInputComponent.getInputStyleClass(),
                !component.isValid() ? INVALID_STYLE_CLASS : null);

        component.getAttributes().put("styleClass", styleClass);
    }
}
