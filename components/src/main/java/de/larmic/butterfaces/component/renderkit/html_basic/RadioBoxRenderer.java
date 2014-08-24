package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlRadioBox;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * larmic butterfaces components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-butterfaces-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlRadioBox.COMPONENT_FAMILY, rendererType = HtmlRadioBox.RENDERER_TYPE)
public class RadioBoxRenderer extends com.sun.faces.renderkit.html_basic.RadioRenderer {

	private final InputRendererSupport inputRendererSupport = new InputRendererSupport();

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		super.encodeBegin(context, component);

		this.inputRendererSupport.encodeBegin(context, (HtmlInputComponent) component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;

		if (!htmlComponent.isReadonly()) {
			super.encodeEnd(context, component);
		}

		this.inputRendererSupport.encodeEnd(context, htmlComponent);
	}
}
