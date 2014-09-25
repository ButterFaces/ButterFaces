package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

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

		sb.append("        <b:secret id=\"input\"\n");
		sb.append("                  label=\"" + this.getLabel() + "\"\n");
		sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);
        this.appendString("componentStyleClass", this.getComponentStyleClass(), sb);
        this.appendString("inoutStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

		this.createAjaxXhtml(sb, "keyup");

		if (this.isValidation()) {
			sb.append("            <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
		}
		sb.append("        </b:secret>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}

    @Override
    protected void addCss(StringBuilder sb) {
        if (!StringUtils.isEmpty(this.getComponentStyleClass())) {
            sb.append(".some-demo-class {\n");
            sb.append("    background-color: red;\n");
            sb.append("}");
        }
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
