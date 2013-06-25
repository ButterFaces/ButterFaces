package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.larmic.jsf2.component.html.AbstractHtmlContainer;

/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
public class AbstractContainerRenderer extends HtmlBasicInputRenderer {

	private static final String LABEL_STYLE_CLASS = "larmic-component-label";
	private static final String REQUIRED_SPAN_CLASS = "larmic-component-required";
	private static final String INPUT_STYLE_CLASS = "larmic-component-input";
	private static final String INVALID_STYLE_CLASS = "input-invalid";
	private static final String TOOLTIP_CLASS = "larmic-component-tooltip";
	private static final String TOOLTIP_LABEL_CLASS = "larmic-component-label-tooltip";

	private static final String FLOATING_STYLE = "display: inline-block;";

	private static final String INPUT_COMPONENT_CLIENT_ID_POSTFIX = "_input";
	private static final String TOOLTIP_DIV_CLIENT_ID_POSTFIX = "_tooltip";

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {

		this.rendererParamsNotNull(context, component);

		if (!this.shouldEncode(component)) {
			return;
		}

		final AbstractHtmlContainer htmlComponent = (AbstractHtmlContainer) component;

		final String style = htmlComponent.getStyle();
		final String styleClass = htmlComponent.getStyleClass();
		final boolean readonly = htmlComponent.getReadonly();
		final boolean required = htmlComponent.isRequired();
		final boolean floating = htmlComponent.getFloating();
		final String label = htmlComponent.getLabel();
		final Object value = htmlComponent.getValue();

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", component);

		this.initInputComponent(htmlComponent, readonly, value);
		this.initOuterDiv(style, styleClass, floating, writer);

		this.writeIdAttributeIfNecessary(context, writer, htmlComponent);
		this.writeLabelIfNecessary(component, htmlComponent, readonly, required, label, writer);

		if (readonly) {
			writer.writeText(value, null);
		}
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		this.rendererParamsNotNull(context, component);

		if (!this.shouldEncodeChildren(component)) {
			return;
		}

		// Render our children recursively
		final Iterator<UIComponent> kids = this.getChildren(component);
		while (kids.hasNext()) {
			this.encodeRecursive(context, kids.next());
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		this.rendererParamsNotNull(context, component);

		if (!this.shouldEncode(component)) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();
		final AbstractHtmlContainer htmlComponent = (AbstractHtmlContainer) component;

		final boolean tooltipNecessary = this.isTooltipNecessary(htmlComponent);

		if (tooltipNecessary || !htmlComponent.isValid()) {
			writer.startElement("span", htmlComponent);
			writer.writeAttribute("id", htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX, null);
			writer.writeAttribute("class", TOOLTIP_CLASS, null);
			writer.writeAttribute("style", this.createOuterTooltipStyle(), null);

			if (tooltipNecessary) {
				writer.startElement("span", htmlComponent);
				writer.writeAttribute("style", "position:relative;left:5px;top:4px;", null);
				writer.writeText(htmlComponent.getTooltip(), null);
				writer.endElement("span");
			}

			if (tooltipNecessary && !context.getMessageList().isEmpty()) {
				writer.startElement("hr", htmlComponent);
				writer.endElement("hr");
				writer.startElement("ul", htmlComponent);
			}

			if (!context.getMessageList().isEmpty()) {
				for (final FacesMessage message : context.getMessageList()) {
					writer.startElement("li", htmlComponent);
					writer.writeText(message.getSummary(), null);
					writer.endElement("li");
				}
				writer.endElement("ul");
			}
			writer.endElement("span");
		}

		writer.endElement("div");
	}

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		this.rendererParamsNotNull(context, component);

		if (!this.shouldDecode(component)) {
			return;
		}

		String clientId = this.decodeBehaviors(context, component);

		if (clientId == null) {
			clientId = component.getClientId(context);
		}

		assert (clientId != null);

		final Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

		final String newValue = requestMap.get(clientId + INPUT_COMPONENT_CLIENT_ID_POSTFIX);

		if (newValue != null) {
			this.setSubmittedValue(component, newValue);
			((AbstractHtmlContainer) component).setValue(newValue);
		}
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	private void initInputComponent(final AbstractHtmlContainer htmlComponent, final boolean readonly,
			final Object value) {
		htmlComponent.getInputComponent().setRendered(!readonly);
		htmlComponent.getInputComponent().setValue(value);
		htmlComponent.getInputComponent().setId(htmlComponent.getId() + INPUT_COMPONENT_CLIENT_ID_POSTFIX);
		htmlComponent
				.getInputComponent()
				.getAttributes()
				.put("onfocus",
						"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
								+ "').style.display = 'inline-block';");
		htmlComponent
				.getInputComponent()
				.getAttributes()
				.put("onblur",
						"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
								+ "').style.display = 'none';");

		if (!htmlComponent.isValid()) {
			htmlComponent.getInputComponent().getAttributes()
					.put("styleClass", INPUT_STYLE_CLASS + " " + INVALID_STYLE_CLASS);
			htmlComponent.getInputComponent().getAttributes().put("style", "background-color: red;");
		} else {
			htmlComponent.getInputComponent().getAttributes().put("styleClass", INPUT_STYLE_CLASS);
		}
	}

	private void initOuterDiv(final String style, final String styleClass, final boolean floating,
			final ResponseWriter writer) throws IOException {
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}
		if (style != null) {
			writer.writeAttribute("style", floating ? FLOATING_STYLE + style : style, null);
		} else if (floating) {
			writer.writeAttribute("style", FLOATING_STYLE, null);
		}
	}

	private void writeLabelIfNecessary(final UIComponent component, final AbstractHtmlContainer htmlComponent,
			final boolean readonly, final boolean required, final String label, final ResponseWriter writer)
			throws IOException {
		if (label != null) {
			writer.startElement("label", component);
			if (!readonly) {
				writer.writeAttribute("for", htmlComponent.getInputComponent().getId(), null);
			}
			if (htmlComponent.getTooltip() != null && !"".equals(htmlComponent.getTooltip())) {
				writer.writeAttribute("class", LABEL_STYLE_CLASS + " " + TOOLTIP_LABEL_CLASS, null);
			} else {
				writer.writeAttribute("class", LABEL_STYLE_CLASS, null);
			}
			writer.writeAttribute("style", "display: inline-block; margin-right: 2px;", null);

			writer.startElement("abbr", component);
			if (this.isTooltipNecessary(htmlComponent)) {
				writer.writeAttribute("title", htmlComponent.getTooltip(), null);
				writer.writeAttribute("style", "cursor: help;", null);
			}
			writer.writeText(htmlComponent.getLabel(), null);
			this.writeRequiredSpanIfNecessary(htmlComponent, readonly, required, writer);
			writer.endElement("abbr");
			writer.endElement("label");
		}
	}

	private void writeRequiredSpanIfNecessary(final AbstractHtmlContainer htmlComponent, final boolean readonly,
			final boolean required, final ResponseWriter writer) throws IOException {
		if (required && !readonly) {
			writer.startElement("span", null);
			writer.writeAttribute("id", htmlComponent.getInputComponent().getClientId() + "_requiredLabel", null);
			writer.writeAttribute("class", REQUIRED_SPAN_CLASS, null);
			writer.writeAttribute("style", "margin-left: 2px; color: red;", null);
			writer.writeText("*", null);
			writer.endElement("span");
		}
	}

	private String createOuterTooltipStyle() {
		final StringBuilder tooltipStyle = new StringBuilder();
		tooltipStyle.append("display: none;");
		tooltipStyle.append("position: absolute;");
		tooltipStyle.append("background-color: white;");
		tooltipStyle.append("border-style: solid;");
		tooltipStyle.append("border-width: 1px;");
		tooltipStyle.append("width: 200px;");
		tooltipStyle.append("z-index: 200;");
		tooltipStyle.append("min-height: 25px;");
		return tooltipStyle.toString();
	}

	private boolean isTooltipNecessary(final AbstractHtmlContainer htmlComponent) {
		return htmlComponent.getTooltip() != null && !"".equals(htmlComponent.getTooltip());
	}
}
