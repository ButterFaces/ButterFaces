package de.larmic.jsf2.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import de.larmic.jsf2.component.html.HtmlCheckBox;
import de.larmic.jsf2.component.html.HtmlInputComponent;

/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 * 
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlCheckBox.COMPONENT_FAMILY, rendererType = HtmlCheckBox.RENDERER_TYPE)
public class CheckBoxRenderer extends com.sun.faces.renderkit.html_basic.CheckboxRenderer {

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
