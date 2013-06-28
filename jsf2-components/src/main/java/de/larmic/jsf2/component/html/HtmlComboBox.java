package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlSelectOneMenu;

@FacesComponent(HtmlComboBox.COMPONENT_TYPE)
public class HtmlComboBox extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.comboBox";

	@Override
	protected UIInput initInputComponent() {
		return new HtmlSelectOneMenu();
	}
}
