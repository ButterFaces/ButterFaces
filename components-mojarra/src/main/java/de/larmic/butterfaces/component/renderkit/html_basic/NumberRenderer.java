package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlNumber;
import de.larmic.butterfaces.component.html.text.HtmlMaskedText;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.AbstractTextRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlNumber.COMPONENT_FAMILY, rendererType = HtmlNumber.RENDERER_TYPE)
public class NumberRenderer extends AbstractTextRenderer<HtmlMaskedText> {

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlNumber numberComponent = (HtmlNumber) component;

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "butterNumberSpinner()", writer, numberComponent);
    }
}