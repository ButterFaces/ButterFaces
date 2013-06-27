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

	@Override
	public String getCss() {
		final StringBuilder sb = new StringBuilder();

		sb.append(".larmic-component-label {\n");
		sb.append("    width: 50px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-required {\n");
		sb.append("    /* nothing */\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-input {\n");
		sb.append("    font-size: 14px !important;\n");
		sb.append("    height: 18px;\n");
		sb.append("    width: 150px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".input-invalid {\n");
		sb.append("    background-color: #FFEBDA !important;\n");
		sb.append("    border-color: #FF0044;\n");
		sb.append("    border-style: solid;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-tooltip {\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    box-shadow: 2px 2px 3px #AAAAAA;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message {\n");
		sb.append("    background-color: #FFEBDA;\n");
		sb.append("    border: 1px solid #FF0044;\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    margin: 5px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message li {\n");
		sb.append("    list-style: disc outside none;\n");
		sb.append("    margin-left: 20px;\n");
		sb.append("    color: #555555;\n");
		sb.append("    font-style: italic;\n");
		sb.append("}\n");

		return sb.toString();
	}

}
