package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;

@FacesComponent(HtmlText.COMPONENT_TYPE)
public class HtmlText extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.text";

	@Override
	protected UIInput initInputComponent() {
		return new HtmlInputText();
	}
}
