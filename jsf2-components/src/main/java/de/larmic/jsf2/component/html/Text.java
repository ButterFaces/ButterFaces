package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;

@FacesComponent(Text.COMPONENT_TYPE)
public class Text extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.text";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.TextRenderer";

	public Text() {
		super(COMPONENT_TYPE, RENDERER_TYPE);
	}

	@Override
	protected UIInput initInputComponent() {
		return new HtmlInputText();
	}
}
