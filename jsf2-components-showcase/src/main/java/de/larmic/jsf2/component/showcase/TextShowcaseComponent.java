package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class TextShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

	private String placeholder;

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

        this.addXhtmlStart(sb);

		sb.append("        <l:text id=\"input\"\n");
		sb.append("                label=\"" + this.getLabel() + "\"\n");
		sb.append("                value=\"" + this.getValue() + "\"\n");
		if (this.getTooltip() != null && !"".equals(this.getTooltip())) {
			sb.append("                tooltip=\"" + this.getTooltip() + "\"\n");
		}
		if (this.getPlaceholder() != null && !"".equals(this.getPlaceholder())) {
			sb.append("                placeholder=\"" + this.getPlaceholder() + "\"\n");
		}
		sb.append("                readonly=\"" + this.isReadonly() + "\"\n");
		sb.append("                required=\"" + this.isRequired() + "\"\n");
		sb.append("                floating=\"" + this.isFloating() + "\"\n");
        sb.append("                disableDefaultStyleClasses=\"" + this.isDisableDefaultStyleClasses() + "\"\n");
		sb.append("                rendered=\"" + this.isRendered() + "\">\n");

		this.createAjaxXhtml(sb, "keyup");

		if (this.isValidation()) {
			sb.append("            <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("        </l:text>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}

	public String getPlaceholder() {
		return this.placeholder;
	}

	public void setPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
	}

}
