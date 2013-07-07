package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.TextRenderer;

import de.larmic.jsf2.component.html.HtmlText;

/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlText.RENDERER_TYPE)
public class BasicInputRenderer extends TextRenderer {

	private final InputRendererSupport inputRendererSupport = new InputRendererSupport();

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		super.encodeBegin(context, component);

		this.inputRendererSupport.encodeBegin(context, (HtmlText) component);
	}

	@Override
	protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
			throws IOException {
		final HtmlText htmlComponent = (HtmlText) component;

		if (!htmlComponent.isReadonly()) {
			super.getEndTextToRender(context, component, currentValue);
		}

		this.inputRendererSupport.getEndTextToRender(context, htmlComponent, currentValue);
	}

}
