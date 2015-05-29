package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.partrenderer.*;
import de.larmic.butterfaces.component.renderkit.html_basic.mojarra.MenuRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlComboBox.RENDERER_TYPE)
public class ComboBoxRenderer extends MenuRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlComboBox comboBox = (HtmlComboBox) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer, "butter-component-combobox");

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(comboBox, writer);

        writer.startElement("div", component);
        writer.writeAttribute("class", "input-group", "styleClass");

        writer.startElement("input", component);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("class", "butter-component-combobox-ghost form-control", "styleClass");
        writer.endElement("input");

        writer.startElement("span", component);
        writer.writeAttribute("class", "input-group-addon pointerCursor", "styleClass");
        writer.startElement("span", component);
        writer.writeAttribute("class", "glyphicon glyphicon-chevron-down", "styleClass");
        writer.endElement("span");
        writer.endElement("span");

        writer.endElement("div");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(component, writer);

        renderTooltipIfNecessary(context, component);

        // Render textarea expandable script call
        new FilterableSelectPartRenderer().renderFilterable(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        new TooltipPartRenderer().renderTooltipIfNecessary(context, component);
    }

    @Override
    protected void renderHtmlFeatures(final UIComponent component, final ResponseWriter writer) throws IOException {
        new HtmlAttributePartRenderer().renderHtmlFeatures(component, writer);
    }
}
