package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlSecret;

import javax.faces.render.FacesRenderer;

@FacesRenderer(componentFamily = HtmlSecret.COMPONENT_FAMILY, rendererType = HtmlSecret.RENDERER_TYPE)
public class SecretRenderer extends AbstractHtmlTagRenderer<HtmlSecret> {

}
