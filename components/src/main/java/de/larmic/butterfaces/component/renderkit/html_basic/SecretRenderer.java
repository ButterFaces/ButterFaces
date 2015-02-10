package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlSecret;

import javax.faces.render.FacesRenderer;

@FacesRenderer(componentFamily = HtmlSecret.COMPONENT_FAMILY, rendererType = HtmlSecret.RENDERER_TYPE)
public class SecretRenderer extends AbstractTextRenderer<HtmlSecret> {

}
