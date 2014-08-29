package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class InnerComponentWrapperPartRenderer {

    public void renderInnerWrapperBegin(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        if (!component.isReadonly()) {
            final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                    Constants.BOOTSTRAP_FORM_CONTROL,
                    component.getInputStyleClass(), !uiComponent.isValid() ? Constants.INVALID_STYLE_CLASS : null);

            uiComponent.getAttributes().put("styleClass", styleClass);

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", component.getHideLabel() ? Constants.BOOTSTRAP_COL_SM_12 : Constants.BOOTSTRAP_COL_SM_10, null);
        }
    }

    public void renderInnerWrapperEnd(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        if (!component.isReadonly()) {
            writer.endElement("div");
        }
    }
}
