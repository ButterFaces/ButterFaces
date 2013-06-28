package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputTextarea;

@FacesComponent(HtmlTextArea.COMPONENT_TYPE)
public class HtmlTextArea extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.textArea";

	@Override
	protected UIInput initInputComponent() {
		return new HtmlInputTextarea();
	}
}
