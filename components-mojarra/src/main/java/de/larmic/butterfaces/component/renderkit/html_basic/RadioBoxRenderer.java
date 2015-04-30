package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlRadioBox;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlRadioBox.COMPONENT_FAMILY, rendererType = HtmlRadioBox.RENDERER_TYPE)
public class RadioBoxRenderer extends de.larmic.butterfaces.component.renderkit.html_basic.mojarra.RadioRenderer {

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

		super.encodeBegin(context, component);

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(htmlComponent, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
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

            // add bootstrap radio class to component
            // bootstrap radio buttons are using pageDirection as default
            // maybe use radio-inline
            writer.startElement("script", component);
            writer.writeText("jQuery(\"table.butter-input-component\").find(\"td\").addClass(\"radio\");", null);
            writer.writeText("jQuery(\"table.butter-input-component\").removeClass(\"form-control\");", null);
            writer.endElement("script");
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        renderTooltipIfNecessary(context, component);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
	}

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        new TooltipPartRenderer().renderTooltipIfNecessary(context, component);
    }
}
