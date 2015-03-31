package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlMaskedText;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlMaskedText.COMPONENT_FAMILY, rendererType = HtmlMaskedText.RENDERER_TYPE)
public class MaskedTextRenderer extends AbstractTextRenderer<HtmlMaskedText> {

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlMaskedText maskedText = (HtmlMaskedText) component;

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", buildPluginCall(maskedText)), null);
        writer.endElement("script");
    }

    private String buildPluginCall(final HtmlMaskedText maskedText) {
        if (StringUtils.isNotEmpty(maskedText.getInputMask())) {
            return "inputmask(" + maskedText.getInputMask() + ")";
        }

        return "inputmask()";
    }
}
