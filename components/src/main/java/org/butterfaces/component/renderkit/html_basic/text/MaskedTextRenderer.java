package org.butterfaces.component.renderkit.html_basic.text;

import org.butterfaces.component.html.text.HtmlMaskedText;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;

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
        final String inputMask = maskedText.getInputMask();
        if (StringUtils.isNotEmpty(inputMask)) {
            if (inputMask.startsWith("'") || inputMask.startsWith("\"")) {
                return "inputmask(" + inputMask + ")";
            } else {
                return "inputmask('" + inputMask + "')";
            }
        }

        return "inputmask()";
    }
}
