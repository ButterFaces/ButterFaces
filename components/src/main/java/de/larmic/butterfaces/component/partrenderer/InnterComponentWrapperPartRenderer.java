package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import java.io.IOException;

/**
 * Created by larmic on 27.08.14.
 */
public class InnterComponentWrapperPartRenderer {

    private static final String INPUT_COMPONENT_MARKER = "butterfaces-input-component";
    private static final String INVALID_STYLE_CLASS = "butterfaces-invalid";

    private static final String BOOTSTRAP_FORM_CONTROL = "form-control";

    public void renderInnerWrapperBegin(final HtmlInputComponent component) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        if (!component.isReadonly()) {
            final String styleClass = StringUtils.concatWithSpace(INPUT_COMPONENT_MARKER, BOOTSTRAP_FORM_CONTROL,
                    component.getInputStyleClass(), !uiComponent.isValid() ? INVALID_STYLE_CLASS : null);

            uiComponent.getAttributes().put("styleClass", styleClass);
        }
    }

}
