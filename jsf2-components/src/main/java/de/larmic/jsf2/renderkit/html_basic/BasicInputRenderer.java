package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.TextRenderer;

import de.larmic.jsf2.component.html.HtmlTextV2;

/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlTextV2.COMPONENT_FAMILY, rendererType = HtmlTextV2.RENDERER_TYPE)
public class BasicInputRenderer extends TextRenderer {

	private static final String LABEL_STYLE_CLASS = "larmic-component-label";
	private static final String REQUIRED_SPAN_CLASS = "larmic-component-required";
	private static final String INPUT_STYLE_CLASS = "larmic-component-input";
	private static final String COMPONENT_INVALID_STYLE_CLASS = "larmic-input-invalid";
	private static final String INVALID_STYLE_CLASS = "input-invalid";
	private static final String TOOLTIP_CLASS = "larmic-component-tooltip";
	private static final String TOOLTIP_LABEL_CLASS = "larmic-component-label-tooltip";
	private static final String ERROR_MESSAGE_CLASS = "larmic-component-error-message";

	private static final String FLOATING_STYLE = "display: inline-block;";
	private static final String NON_FLOATING_STYLE = "display: table;";

	private static final String TOOLTIP_DIV_CLIENT_ID_POSTFIX = "_tooltip";

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		super.encodeBegin(context, component);

		if (!this.shouldEncode(component)) {
			return;
		}

		final HtmlTextV2 htmlComponent = (HtmlTextV2) component;

		final String style = htmlComponent.getStyle();
		final String styleClass = htmlComponent.getStyleClass();
		final boolean readonly = htmlComponent.getReadonly();
		final boolean required = htmlComponent.isRequired();
		final boolean floating = htmlComponent.getFloating();
		final boolean valid = htmlComponent.isValid();
		final String label = htmlComponent.getLabel();
		final Object value = htmlComponent.getValue();

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", component);

		this.initOuterDiv(style, styleClass, floating, valid, writer);
		this.writeLabelIfNecessary(htmlComponent, readonly, required, label, writer);

		if (readonly) {
			writer.writeText(this.getReadonlyDisplayValue(value, htmlComponent, htmlComponent.getConverter()), null);
		}

		this.initImputComponent(htmlComponent);
	}

	@Override
	protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
			throws IOException {
		final HtmlTextV2 htmlComponent = (HtmlTextV2) component;
		final ResponseWriter writer = context.getResponseWriter();

		if (!htmlComponent.isReadonly()) {
			super.getEndTextToRender(context, component, currentValue);
		}

		final boolean tooltipNecessary = this.isTooltipNecessary(htmlComponent);

		if ((tooltipNecessary || !htmlComponent.isValid()) && !htmlComponent.getReadonly()) {
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

			if (!context.getMessageList().isEmpty()) {
				writer.startElement("div", htmlComponent);
				writer.writeAttribute("class", ERROR_MESSAGE_CLASS, null);
				writer.startElement("ul", htmlComponent);
				for (final FacesMessage message : context.getMessageList()) {
					writer.startElement("li", htmlComponent);
					writer.writeText(message.getSummary(), null);
					writer.endElement("li");
				}
				writer.endElement("ul");
				writer.endElement("div");
			}
			writer.endElement("span");
		}

		writer.endElement("div");
	}

	/**
	 * Should return value string for the readonly view mode. Can be overridden
	 * for custom components.
	 * 
	 * @return the value string for the readonly view mode
	 */
	protected String getReadonlyDisplayValue(final Object value, final HtmlTextV2 component, final Converter converter) {
		if (value == null) {
			return "-";
		} else if (converter != null) {
			return converter.getAsString(FacesContext.getCurrentInstance(), component, value);
		}

		return String.valueOf(value);
	}

	private void writeLabelIfNecessary(final HtmlTextV2 component, final boolean readonly, final boolean required,
			final String label, final ResponseWriter writer) throws IOException {
		if (label != null) {
			writer.startElement("label", component);
			if (!readonly) {
				writer.writeAttribute("for", component.getId(), null);
			}
			if (component.getTooltip() != null && !"".equals(component.getTooltip())) {
				writer.writeAttribute("class", this.concatStyles(LABEL_STYLE_CLASS, TOOLTIP_LABEL_CLASS), null);
			} else {
				writer.writeAttribute("class", LABEL_STYLE_CLASS, null);
			}
			writer.writeAttribute("style", "display: inline-block; margin-right: 2px; float: left;", null);

			writer.startElement("abbr", component);
			if (this.isTooltipNecessary(component)) {
				writer.writeAttribute("title", component.getTooltip(), null);
				writer.writeAttribute("style", "cursor: help; word-wrap: breaking-word;", null);
			}
			writer.writeText(component.getLabel(), null);
			writer.endElement("abbr");

			this.writeRequiredSpanIfNecessary(component, readonly, required, writer);

			writer.endElement("label");
		}
	}

	private void initOuterDiv(final String style, final String styleClass, final boolean floating, final boolean valid,
			final ResponseWriter writer) throws IOException {
		if (styleClass != null) {
			writer.writeAttribute("class",
					valid ? styleClass : this.concatStyles(COMPONENT_INVALID_STYLE_CLASS, styleClass), null);
		} else if (!valid) {
			writer.writeAttribute("class", COMPONENT_INVALID_STYLE_CLASS, null);
		}
		if (style != null) {
			final String floatingStyle = floating ? FLOATING_STYLE : NON_FLOATING_STYLE;
			writer.writeAttribute("style", this.concatStyles(floatingStyle, style), null);
		} else {
			writer.writeAttribute("style", floating ? FLOATING_STYLE : NON_FLOATING_STYLE, null);
		}
	}

	private void writeRequiredSpanIfNecessary(final HtmlTextV2 htmlComponent, final boolean readonly,
			final boolean required, final ResponseWriter writer) throws IOException {
		if (required && !readonly) {
			writer.startElement("span", null);
			writer.writeAttribute("id", htmlComponent.getClientId() + "_requiredLabel", null);
			writer.writeAttribute("class", REQUIRED_SPAN_CLASS, null);
			writer.writeAttribute("style", "margin-left: 2px; color: red;", null);
			writer.writeText("*", null);
			writer.endElement("span");
		}
	}

	private void initImputComponent(final HtmlTextV2 htmlComponent) {
		htmlComponent.getAttributes().put(
				"onfocus",
				"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
						+ "').style.display = 'inline-block';");
		htmlComponent.getAttributes().put(
				"onblur",
				"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
						+ "').style.display = 'none';");
		if (!htmlComponent.isValid()) {
			htmlComponent.getAttributes().put("styleClass", INPUT_STYLE_CLASS + " " + INVALID_STYLE_CLASS);
			htmlComponent.getAttributes().put("style", "background-color: red;");
		} else {
			htmlComponent.getAttributes().put("styleClass", INPUT_STYLE_CLASS);
		}
	}

	private boolean isTooltipNecessary(final HtmlTextV2 htmlComponent) {
		return htmlComponent.getTooltip() != null && !"".equals(htmlComponent.getTooltip());
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

	private String concatStyles(final String... styles) {
		final StringBuilder sb = new StringBuilder();

		for (final String style : styles) {
			sb.append(style);
			sb.append(" ");
		}

		return sb.toString();
	}
}
