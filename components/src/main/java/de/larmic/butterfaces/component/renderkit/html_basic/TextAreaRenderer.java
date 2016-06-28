package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;
import de.larmic.butterfaces.component.partrenderer.ExpandablePartRenderer;
import de.larmic.butterfaces.component.partrenderer.MaxLengthPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

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
        // Render textarea counter
        new MaxLengthPartRenderer().renderMaxLength(component, writer);

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
