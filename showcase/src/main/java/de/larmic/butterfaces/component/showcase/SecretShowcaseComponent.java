package de.larmic.butterfaces.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class SecretShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

	private String placeholder = DEFAULT_TEXT_PLACEHOLDER;

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

        this.addXhtmlStart(sb);

		sb.append("        <l:secret id=\"input\"\n");
		sb.append("                  label=\"" + this.getLabel() + "\"\n");
		sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

		this.createAjaxXhtml(sb, "keyup");

		if (this.isValidation()) {
			sb.append("            <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("        </l:secret>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}

    protected String getEmptyDistanceString() {
        return "                  ";
    }

	public String getPlaceholder() {
		return this.placeholder;
	}

	public void setPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
	}

}
