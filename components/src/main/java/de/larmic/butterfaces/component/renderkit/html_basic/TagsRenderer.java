package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlTags;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlTags.COMPONENT_FAMILY, rendererType = HtmlTags.RENDERER_TYPE)
public class TagsRenderer extends TextRenderer {

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTags htmlTags = (HtmlTags) component;

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", createJQueryPluginCall(htmlTags)), null);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", createJQueryPluginCallback(htmlTags)), null);
        writer.endElement("script");
    }

    private String createJQueryPluginCallback(HtmlTags tags) {
        final StringBuilder jQueryPluginCall = new StringBuilder();
        jQueryPluginCall.append("on(\"itemAdded\", function (e) {");
        jQueryPluginCall.append(RenderUtils.createJQueryBySelector(tags.getClientId(), ".butter-input-component") + "trigger('keyup');");
        jQueryPluginCall.append("})");
        jQueryPluginCall.append(".on(\"itemRemoved\", function (e) {");
        jQueryPluginCall.append(RenderUtils.createJQueryBySelector(tags.getClientId(), ".butter-input-component") + "trigger('keyup');");
        jQueryPluginCall.append("})");
        return jQueryPluginCall.toString();
    }

    private String createJQueryPluginCall(HtmlTags tags) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        jQueryPluginCall.append("tagsinput()");
        return jQueryPluginCall.toString();
    }
}
