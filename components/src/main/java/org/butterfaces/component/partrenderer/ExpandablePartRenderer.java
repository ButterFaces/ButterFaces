package org.butterfaces.component.partrenderer;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.HtmlTextArea;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ExpandablePartRenderer {

    public void renderExpandable(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId();

        if (isExpandableNecessary(component)) {
            RenderUtils.renderJQueryPluginCall(outerComponentId, "butterExpandable()", writer, uiComponent);
        }
    }

    private boolean isExpandableNecessary(final HtmlInputComponent component) {
        return Boolean.TRUE.equals(((HtmlTextArea) component).getExpandable());
    }
}
