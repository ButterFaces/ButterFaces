package de.larmic.jsf2.component.html;

import java.util.ArrayList;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
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
				@SuppressWarnings("unchecked")
				final ArrayList<SelectItem> items = (ArrayList<SelectItem>) ((UISelectItems) uiComponent).getValue();

				for (final SelectItem item : items) {
					if (this.isItemMatching(item, value)) {
						return value;
					}
				}
			} else if (uiComponent instanceof UISelectItem) {
				if (this.isItemMatching((SelectItem) ((UISelectItem) uiComponent).getValue(), value)) {
					return value;
				}
			}
		}
		return value;
	}

	@Override
	protected UIInput initInputComponent() {
		return new HtmlSelectOneMenu();
	}

	private boolean isItemMatching(final SelectItem item, final Object value) {
		final Object itemValue = item.getValue();

		return itemValue != null && itemValue.toString().equals(value);
	}
}
