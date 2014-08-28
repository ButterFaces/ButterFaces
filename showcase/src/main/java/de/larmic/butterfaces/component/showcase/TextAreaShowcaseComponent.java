package de.larmic.butterfaces.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class TextAreaShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

	private Integer maxLength;

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

		sb.append("        <l:textArea id=\"input\"\n");
		sb.append("                    label=\"" + this.getLabel() + "\"\n");
		sb.append("                    value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);

		if (this.getMaxLength() != null) {
			sb.append("                    maxLength=\"" + this.getMaxLength() + "\"\n");
		}

        this.appendBoolean("rendered", this.isRendered(), sb, true);

		this.createAjaxXhtml(sb, "keyup");

		if (this.isValidation()) {
			sb.append("            <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("        </l:textArea>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}

    protected String getEmptyDistanceString() {
        return "                    ";
    }

    public Integer getMaxLength() {
		return this.maxLength;
	}

	public void setMaxLength(final Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getPlaceholder() {
		return this.placeholder;
	}

	public void setPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
	}
}
