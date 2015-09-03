package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlMarkdown;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlMarkdown.COMPONENT_FAMILY, rendererType = HtmlMarkdown.RENDERER_TYPE)
public class MarkdownRenderer extends TextAreaRenderer {

    @Override
    protected void renderAdditionalScript(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final HtmlMarkdown markdown = (HtmlMarkdown) component;

        writer.startElement("script", markdown);
        if (!markdown.isReadonly()) {
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), "textarea", createJQueryMarkdownPluginCall(markdown)), null);
        } else {
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), null, createJQueryMarkdownToHtmlPluginCall()), null);
        }
        writer.endElement("script");
    }

    @Override
    protected void renderExpandable(final HtmlInputComponent htmlComponent, final ResponseWriter writer) throws IOException {
        // do nothing. no expandable support!
    }

    @Override
    protected String getComponentNameStyleClass() {
        return "butter-component-markdown";
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

    private String createJQueryMarkdownToHtmlPluginCall() {
        return "markdownReadonly()";
    }
}
