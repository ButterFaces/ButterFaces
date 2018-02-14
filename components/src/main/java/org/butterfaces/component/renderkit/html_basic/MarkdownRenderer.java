package org.butterfaces.component.renderkit.html_basic;

import org.butterfaces.component.html.HtmlMarkdown;
import org.butterfaces.component.partrenderer.MaxLengthPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.MaxLengthPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import org.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlMarkdown.COMPONENT_FAMILY, rendererType = HtmlMarkdown.RENDERER_TYPE)
public class MarkdownRenderer extends AbstractHtmlTagRenderer<HtmlMarkdown> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-markdown");
    }

    @Override
    protected String getHtmlTagName() {
        return "textarea";
    }

    @Override
    protected void encodeEnd(final HtmlMarkdown markdown, final ResponseWriter writer) throws IOException {
        // Render textarea counter
        new MaxLengthPartRenderer().renderMaxLength(markdown, writer);

        writer.startElement("script", markdown);
        if (!markdown.isReadonly()) {
            writer.writeText(RenderUtils.createJQueryPluginCall(markdown.getClientId(), "textarea", createJQueryMarkdownPluginCall(markdown)), null);
        } else {
            writer.writeText(RenderUtils.createJQueryPluginCall(markdown.getClientId(), null, "markdownReadonly()"), null);
        }
        writer.endElement("script");

        if (StringUtils.isNotEmpty(markdown.getPlaceholder())
                && markdown.getPlaceholder().contains("\\n")
                && !markdown.isReadonly()) {
            RenderUtils.renderJQueryPluginCall(markdown.getClientId(), "multilinePlaceholder()", writer, markdown);
        }
    }

    private String createJQueryMarkdownPluginCall(HtmlMarkdown markdown) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        jQueryPluginCall.append("markdown({");
        jQueryPluginCall.append("autofocus: false,");
        jQueryPluginCall.append("savable: false,");
        jQueryPluginCall.append("language: '" + markdown.getLanguage() + "'");
        jQueryPluginCall.append("})");

        return jQueryPluginCall.toString();
    }

}
