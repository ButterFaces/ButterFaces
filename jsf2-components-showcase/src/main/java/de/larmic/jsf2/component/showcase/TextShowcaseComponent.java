package de.larmic.jsf2.component.showcase;

public class TextShowcaseComponent extends AbstractShowcaseComponent {

	@Override
	protected Object initValue() {
		return "value";
	}

	@Override
	public String getReadableValue() {
		return (String) this.getValue();
	}

	@Override
	public String getXHtml() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<l:text id=\"input\"\n");
		sb.append("        label=\"" + this.getLabel() + "\"\n");
		sb.append("        value=\"" + this.getValue() + "\"\n");
		if (this.getTooltip() != null && !"".equals(this.getTooltip())) {
			sb.append("        tooltip=\"" + this.getTooltip() + "\"\n");
		}
		sb.append("        readonly=\"" + this.isReadonly() + "\"\n");
		sb.append("        required=\"" + this.isRequired() + "\"\n");
		sb.append("        floating=\"" + this.isFloating() + "\"\n");
		sb.append("        rendered=\"" + this.isRendered() + "\">\n");

		if (this.isAjax()) {
			sb.append("    <f:ajax event=\"keyup\" \n");
			sb.append("            execute=\"input\"\n");
			sb.append("            render=\"output\"/>\n");
		}
		if (this.isValidation()) {
			sb.append("    <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("</l:text>");

		if (this.isAjax()) {
			sb.append("\n");
			sb.append("<h:outputText id=\"output\" value=\"" + this.getValue() + "\"/>");
		}
		return sb.toString();
	}
}
