package de.larmic.jsf2.component.html;

import java.util.ArrayList;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.model.SelectItem;

@FacesComponent(HtmlComboBox.COMPONENT_TYPE)
public class HtmlComboBox extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.comboBox";

	@Override
	public Object encodeValue(final Object value) {
		for (final UIComponent uiComponent : this.getInputComponent().getChildren()) {
			if (uiComponent instanceof UISelectItems) {
				final ArrayList<SelectItem> items = (ArrayList<SelectItem>) ((UISelectItems) uiComponent).getValue();

				for (final SelectItem item : items) {
					final Object itemValue = item.getValue();

					if (itemValue != null && itemValue.toString().equals(value)) {
						return itemValue;
					}
				}
			}
		}
		return value;
	}

	@Override
	protected UIInput initInputComponent() {
		return new HtmlSelectOneMenu();
	}
}
