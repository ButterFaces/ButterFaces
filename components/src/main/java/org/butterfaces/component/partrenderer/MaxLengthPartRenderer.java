package org.butterfaces.component.partrenderer;

import java.io.IOException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.feature.MaxLength;
import org.butterfaces.resolver.WebXmlParameters;

public class MaxLengthPartRenderer {

    public void renderMaxLength(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final String outerComponentId = component.getClientId();

        if (isMaxLengthNecessary(component)) {
            final String maxLengthText = new WebXmlParameters(FacesContext.getCurrentInstance().getExternalContext()).getMaxLengthText();
            final Integer maxLength = ((MaxLength) component).getMaxLength();

            RenderUtils
                .renderJQueryPluginCall(outerComponentId, "butterMaxLength({maxLength: " + maxLength + ", maxLengthText: '" + maxLengthText + "'})", writer, uiComponent);
        }
    }

    private boolean isMaxLengthNecessary(final HtmlInputComponent component) {
        return component instanceof MaxLength && ((MaxLength) component).getMaxLength() != null;
    }
}
