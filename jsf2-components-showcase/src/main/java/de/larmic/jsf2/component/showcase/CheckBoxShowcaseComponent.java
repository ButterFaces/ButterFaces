package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class CheckBoxShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

	@Override
	protected Object initValue() {
		return false;
	}

	@Override
	public String getReadableValue() {
		return (Boolean) this.getValue() ? "Ja" : "Nein";
	}

	@Override
	public String getXHtml() {
		final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

		sb.append("        <l:checkBox id=\"input\"\n");
		sb.append("                    label=\"" + this.getLabel() + "\"\n");
		sb.append("                    value=\"" + this.getValue() + "\"\n");
		if (this.getTooltip() != null && !"".equals(this.getTooltip())) {
			sb.append("                    tooltip=\"" + this.getTooltip() + "\"\n");
		}
		sb.append("                    readonly=\"" + this.isReadonly() + "\"\n");
		sb.append("                    required=\"" + this.isRequired() + "\"\n");
		sb.append("                    floating=\"" + this.isFloating() + "\"\n");
		sb.append("                    rendered=\"" + this.isRendered() + "\">\n");

		this.createAjaxXhtml(sb, "change");

		sb.append("        </l:checkBox>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}
}
