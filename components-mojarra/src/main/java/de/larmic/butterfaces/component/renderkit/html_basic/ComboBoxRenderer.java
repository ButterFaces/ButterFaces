package de.larmic.butterfaces.component.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.partrenderer.HtmlAttributePartRenderer;
import de.larmic.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.LabelPartRenderer;
import de.larmic.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.ReadonlyPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.TooltipPartRenderer;
import de.larmic.butterfaces.component.renderkit.html_basic.mojarra.MenuRenderer;

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

        if (!comboBox.isReadonly()) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "input-group", "styleClass");

            final UIComponent inputGroupAddonLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_LEFT);
            final UIComponent inputGroupBtnLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_LEFT);
            if (comboBox.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON) && inputGroupAddonLeftFacet != null) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-addon", null);
                inputGroupAddonLeftFacet.encodeAll(context);
                writer.endElement("span");
            }
            if (comboBox.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN) && inputGroupBtnLeftFacet != null) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-btn", null);
                inputGroupBtnLeftFacet.encodeAll(context);
                writer.endElement("span");
            }

            writer.startElement("input", component);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("autocomplete", "off", null);
            writer.writeAttribute("class", "butter-component-combobox-ghost form-control", "styleClass");
            writer.endElement("input");

            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-addon cursor-pointer", "styleClass");
            writer.startElement("span", component);
            writer.writeAttribute("class", "glyphicon glyphicon-chevron-down", "styleClass");
            writer.endElement("span");
            writer.endElement("span");

            writer.endElement("div");
        }
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

        if (!htmlComponent.isReadonly()) {
            // Render textarea expandable script call
            RenderUtils.renderJQueryPluginCall(component.getClientId(), "butterCombobox()", writer, component);
        }

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
