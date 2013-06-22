package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.larmic.jsf2.component.html.AbstractHtmlContainer;

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
		final boolean required = htmlComponent.getRequired();
		final boolean floating = htmlComponent.getFloating();
		final String label = htmlComponent.getLabel();
		final Object value = htmlComponent.getValue();

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", component);

		this.writeIdAttributeIfNecessary(context, writer, htmlComponent);
		htmlComponent.getInputComponent().setRendered(!readonly);
		htmlComponent.getInputComponent().setValue(value);
		htmlComponent.getInputComponent().setId(htmlComponent.getId() + INPUT_COMPONENT_CLIENT_ID_POSTFIX);
		htmlComponent
				.getInputComponent()
				.getAttributes()
				.put("onfocus",
						"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
								+ "').style.display = 'block';");
		htmlComponent
				.getInputComponent()
				.getAttributes()
				.put("onblur",
						"document.getElementById('" + htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX
								+ "').style.display = 'none';");

		if (!htmlComponent.isValid()) {
			htmlComponent.getInputComponent().getAttributes()
					.put("styleClass", INPUT_STYLE_CLASS + " " + INVALID_STYLE_CLASS);
		} else {
			htmlComponent.getInputComponent().getAttributes().put("styleClass", INPUT_STYLE_CLASS);
		}

		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}
		if (style != null) {
			writer.writeAttribute("style", floating ? FLOATING_STYLE + style : style, null);
		} else if (floating) {
			writer.writeAttribute("style", FLOATING_STYLE, null);
		}
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
			writer.writeAttribute("title", htmlComponent.getTooltip(), null);
			writer.writeText(htmlComponent.getLabel(), null);
			writer.endElement("label");
		}
		if (required && !readonly) {
			writer.startElement("span", null);
			writer.writeAttribute("id", htmlComponent.getInputComponent().getClientId() + "_requiredLabel", null);
			writer.writeAttribute("class", REQUIRED_SPAN_CLASS, "class");
			writer.writeText("*", null);
			writer.endElement("span");
		}

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

		if (htmlComponent.getTooltip() != null && !"".equals(htmlComponent.getTooltip())) {
			writer.startElement("span", htmlComponent);
			writer.writeAttribute("id", htmlComponent.getId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX, null);
			writer.writeAttribute("class", TOOLTIP_CLASS, null);
			writer.writeAttribute("style", "position: relative; display: none;", null);
			writer.writeText(htmlComponent.getTooltip(), null);
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
		}
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
