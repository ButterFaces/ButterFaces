package org.butterfaces.component.renderkit.html_basic.text;

import org.butterfaces.component.html.text.HtmlSecret;

import javax.faces.render.FacesRenderer;

@FacesRenderer(componentFamily = HtmlSecret.COMPONENT_FAMILY, rendererType = HtmlSecret.RENDERER_TYPE)
public class SecretRenderer extends AbstractHtmlTagRenderer<HtmlSecret> {

}
