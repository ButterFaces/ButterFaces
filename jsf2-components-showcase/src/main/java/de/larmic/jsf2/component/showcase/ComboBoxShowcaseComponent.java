package de.larmic.jsf2.component.showcase;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ComboBoxShowcaseComponent extends AbstractShowcaseComponent {

	private ComboBoxValueType comboBoxValueType = ComboBoxValueType.STRING;

	@Override
	protected Object initValue() {
		return null;
	}

	@Override
	public String getReadableValue() {
		return (String) this.getValue();
	}

	@Override
	public String getXHtml() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<l:comboBox id=\"input\"\n");
		sb.append("        	   label=\"" + this.getLabel() + "\"\n");
		sb.append("        	   value=\"" + this.getValue() + "\"\n");
		if (this.getTooltip() != null && !"".equals(this.getTooltip())) {
			sb.append("        	   tooltip=\"" + this.getTooltip() + "\"\n");
		}
		sb.append("        	   readonly=\"" + this.isReadonly() + "\"\n");
		sb.append("        	   required=\"" + this.isRequired() + "\"\n");
		sb.append("        	   floating=\"" + this.isFloating() + "\"\n");
		sb.append("        	   rendered=\"" + this.isRendered() + "\">\n");

		sb.append("    <f:selectItem itemValue=\"#{null}\" \n");
		sb.append("                  itemLabel=\"Choose one...\"/>\n");
		sb.append("    <f:selectItem itemValue=\"2000\" \n");
		sb.append("                  itemLabel=\"Year 2000\"/>\n");
		sb.append("    <f:selectItem itemValue=\"2010\" \n");
		sb.append("                  itemLabel=\"Year 2010\"/>\n");
		sb.append("    <f:selectItem itemValue=\"2020\" \n");
		sb.append("                  itemLabel=\"Year 2020\"/>\n");

		this.createAjaxXhtml(sb, "change");

		/*
		 * if (this.isValidation()) {
		 * sb.append("    <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		 * }
		 */

		sb.append("</l:comboBox>");

		this.createOutputXhtml(sb);

		return sb.toString();
	}

	public List<SelectItem> getComboBoxTypes() {
		final List<SelectItem> items = new ArrayList<SelectItem>();

		for (final ComboBoxValueType type : ComboBoxValueType.values()) {
			items.add(new SelectItem(type, type.label));
		}
		return items;
	}

	public ComboBoxValueType getComboBoxValueType() {
		return this.comboBoxValueType;
	}

	public void setComboBoxValueType(final ComboBoxValueType comboBoxValueType) {
		this.comboBoxValueType = comboBoxValueType;
	}

}
