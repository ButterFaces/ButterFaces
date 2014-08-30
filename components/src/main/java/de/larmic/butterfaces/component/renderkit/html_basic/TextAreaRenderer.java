package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * larmic butterfaces components - An jsf 2 component extension https://bitbucket.org/larmicBB/larmic-butterfaces-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlTextArea.COMPONENT_FAMILY, rendererType = HtmlTextArea.RENDERER_TYPE)
public class TextAreaRenderer extends com.sun.faces.renderkit.html_basic.TextareaRenderer {

	private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXTAREA);

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
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
        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        // Render textarea counter
        new CharacterCounterPartRenderer().renderCharacterCounter(htmlComponent, writer);

        // render tooltip elements if necessary
        new TooltipPartRenderer().renderTooltip(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
	}

	/**
	 * Method copied from super class to add html features.
	 */
	@Override
	protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
			throws IOException {

		final ResponseWriter writer = context.getResponseWriter();
		assert (writer != null);

		final String styleClass = (String) component.getAttributes().get("styleClass");

		writer.startElement("textarea", component);
		this.writeIdAttributeIfNecessary(context, writer, component);
		writer.writeAttribute("name", component.getClientId(context), "clientId");
		if (null != styleClass) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}

		// *** BEGIN HTML 5 CHANGED **************************
		if (component instanceof HtmlTextArea) {
			final HtmlTextArea inputComponent = (HtmlTextArea) component;
			if (inputComponent.getPlaceholder() != null && !"".equals(inputComponent.getPlaceholder())) {
				writer.writeAttribute("placeholder", inputComponent.getPlaceholder(), "placeholder");
			}
		}
		// *** END HTML 5 CHANGED ****************************

		// style is rendered as a passthru attribute
		RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES);
		RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

		RenderKitUtils.renderOnchange(context, component, false);

		// render default text specified
		if (currentValue != null) {
			writer.writeText(currentValue, component, "value");
		}

		writer.endElement("textarea");

	}
}
