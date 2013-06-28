package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

@FacesComponent(HtmlCheckBox.COMPONENT_TYPE)
public class HtmlCheckBox extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.checkBox";

	@Override
	protected UIInput initInputComponent() {
		return new HtmlSelectBooleanCheckbox();
	}
}
