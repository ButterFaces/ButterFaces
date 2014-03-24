package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class SecretShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

	private String placeholder = "enter password";

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

		sb.append("        <l:secret id=\"input\"\n");
		sb.append("                  label=\"" + this.getLabel() + "\"\n");
		sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);
        this.appendBoolean("disableDefaultStyleClasses", this.isDisableDefaultStyleClasses(), sb);

        if (this.isBootstrap()) {
            this.appendString("componentStyleClass", "form-group", sb);
            this.appendString("inputStyleClass", "form-control", sb);
        }

        this.appendBoolean("rendered", this.isRendered(), sb, true);

		this.createAjaxXhtml(sb, "keyup");

        if (getFacetText() != null && !"".equals(getFacetText())) {
            sb.append("            " + "<f:facet name=\"input-container\">\n");
            sb.append("            " + "    " + getFacetText() + "\n");
            sb.append("            " + "</f:facet>\n");
        }

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
