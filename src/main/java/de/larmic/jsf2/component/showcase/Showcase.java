package de.larmic.jsf2.component.showcase;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class Showcase implements Serializable {

	private boolean readonlyTextComponent;
	private boolean requiredTextComponent;
	private boolean floatingTextComponent;
	private boolean validateTextComponent;
	private String tooltipTextComponent;
	private String labelTextComponent = "label";
	private String valueTextComponent = "value";

	public boolean isReadonlyTextComponent() {
		return this.readonlyTextComponent;
	}

	public void setReadonlyTextComponent(final boolean readonlyTextComponent) {
		this.readonlyTextComponent = readonlyTextComponent;
	}

	public boolean isRequiredTextComponent() {
		return this.requiredTextComponent;
	}

	public void setRequiredTextComponent(final boolean requiredTextComponent) {
		this.requiredTextComponent = requiredTextComponent;
	}

	public boolean isFloatingTextComponent() {
		return this.floatingTextComponent;
	}

	public void setFloatingTextComponent(final boolean floatingTextComponent) {
		this.floatingTextComponent = floatingTextComponent;
	}

	public String getLabelTextComponent() {
		return this.labelTextComponent;
	}

	public void setLabelTextComponent(final String labelTextComponent) {
		this.labelTextComponent = labelTextComponent;
	}

	public String getValueTextComponent() {
		return this.valueTextComponent;
	}

	public void setValueTextComponent(final String valueTextComponent) {
		this.valueTextComponent = valueTextComponent;
	}

	public boolean isValidateTextComponent() {
		return this.validateTextComponent;
	}

	public void setValidateTextComponent(final boolean validateTextComponent) {
		this.validateTextComponent = validateTextComponent;
	}

	public String getTooltipTextComponent() {
		return this.tooltipTextComponent;
	}

	public void setTooltipTextComponent(final String tooltipTextComponent) {
		this.tooltipTextComponent = tooltipTextComponent;
	}

	public String getTextComponentCode() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<l:text label=\"" + this.labelTextComponent + "\"\n");
		sb.append("        value=\"" + this.valueTextComponent + "\"\n");
		if (this.tooltipTextComponent != null && !"".equals(this.tooltipTextComponent)) {
			sb.append("        tooltip=\"" + this.tooltipTextComponent + "\"\n");
		}
		sb.append("        readonly=\"" + this.readonlyTextComponent + "\"\n");
		sb.append("        required=\"" + this.requiredTextComponent + "\"\n");
		sb.append("        floating=\"" + this.floatingTextComponent + "\">\n");
		if (this.validateTextComponent) {
			sb.append("    <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("</l:text>");
		return sb.toString();
	}

	public String getTextComponentCSS() {
		final StringBuilder sb = new StringBuilder();

		sb.append(".larmic-component-label {\n");
		sb.append("    margin-right: 2px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-required {\n");
		sb.append("    color: red;\n");
		sb.append("    margin-right: 2px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-input {\n");
		sb.append("    font-size: 14px !important;\n");
		sb.append("    height: 18px;\n");
		sb.append("    margin-right: 2px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".input-invalid {\n");
		sb.append("    background-color: #CC6633 !important;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-tooltip li {\n");
		sb.append("    list-style: disc outside none;\n");
		sb.append("    margin-left: 20px;\n");
		sb.append("    color: #CC6633;\n");
		sb.append("}\n");

		return sb.toString();
	}
}
