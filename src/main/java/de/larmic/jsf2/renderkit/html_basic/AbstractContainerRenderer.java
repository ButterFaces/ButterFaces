package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

import de.larmic.jsf2.component.html.Text;

public class AbstractContainerRenderer extends HtmlBasicRenderer {
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {

		this.rendererParamsNotNull(context, component);

		if (!this.shouldEncode(component)) {
			return;
		}
		// Render a span around this group if necessary
		final String style = (String) component.getAttributes().get("style");
		final String styleClass = (String) component.getAttributes().get("styleClass");
		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", component);

		this.writeIdAttributeIfNecessary(context, writer, component);

		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}
		if (style != null) {
			writer.writeAttribute("style", style, "style");
		}

		final Text textComponent = (Text) component;

		writer.startElement("div", component);
		writer.writeText(textComponent.getLabel(), textComponent.getLabel());
		writer.endElement("div");

		final boolean readonly = textComponent.isReadonly();

		textComponent.getReadonlyComponent().setRendered(readonly);
		textComponent.getInputComponent().setRendered(!readonly);
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
		writer.endElement("div");
	}

	@Override
	public boolean getRendersChildren() {

		return true;

	}
}
