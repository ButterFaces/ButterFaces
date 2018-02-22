package org.butterfaces.component.renderkit.html_basic;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.HtmlTextArea;
import org.butterfaces.component.partrenderer.ExpandablePartRenderer;
import org.butterfaces.component.partrenderer.MaxLengthPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.ExpandablePartRenderer;
import org.butterfaces.component.partrenderer.MaxLengthPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import org.butterfaces.util.StringUtils;

@FacesRenderer(componentFamily = HtmlTextArea.COMPONENT_FAMILY, rendererType = HtmlTextArea.RENDERER_TYPE)
public class TextAreaRenderer extends AbstractHtmlTagRenderer<HtmlTextArea> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        encodeBegin(context, component, "butter-component-textarea");
    }

    @Override
    protected String getHtmlTagName() {
        return "textarea";
    }

    @Override
    protected void encodeEnd(final HtmlTextArea component, final ResponseWriter writer) throws IOException {

        // TODO maxlength doesn't work with expandable actually
        if (!Boolean.TRUE.equals(component.getExpandable())) {
            // Render textarea counter
            new MaxLengthPartRenderer().renderMaxLength(component, writer);
        }

        // Render textarea expandable script call
        renderExpandable(component, writer);

        if (StringUtils.isNotEmpty(component.getPlaceholder())
            && component.getPlaceholder().contains("\\n")
            && !component.isReadonly()) {
            RenderUtils.renderJQueryPluginCall(component.getClientId(), "multilinePlaceholder()", writer, component);
        }
    }

    protected void renderExpandable(HtmlInputComponent htmlComponent, ResponseWriter writer) throws IOException {
        new ExpandablePartRenderer().renderExpandable(htmlComponent, writer);
    }
}
