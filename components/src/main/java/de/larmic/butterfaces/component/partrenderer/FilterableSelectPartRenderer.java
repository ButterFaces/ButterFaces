package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class FilterableSelectPartRenderer {

    public void renderFilterable(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId();

        RenderUtils.renderJQueryPluginCall(outerComponentId, "butterFilterableSelect()", writer, uiComponent);
    }

}
