package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlMaskedText;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlMaskedText.COMPONENT_FAMILY, rendererType = HtmlMaskedText.RENDERER_TYPE)
public class MaskedTextRenderer extends AbstractHtmlTagRenderer<HtmlMaskedText> {

    @Override
    protected void encodeEnd(HtmlMaskedText maskedText, ResponseWriter writer) throws IOException {
        writer.startElement("script", maskedText);
        writer.writeText(RenderUtils.createJQueryPluginCall(maskedText.getClientId(), ".butter-input-component", buildPluginCall(maskedText)), null);
        writer.endElement("script");
    }

    private String buildPluginCall(final HtmlMaskedText maskedText) {
        if (StringUtils.isNotEmpty(maskedText.getInputMask())) {
            return "inputmask('" + maskedText.getInputMask() + "')";
        }

        return "inputmask()";
    }
}
