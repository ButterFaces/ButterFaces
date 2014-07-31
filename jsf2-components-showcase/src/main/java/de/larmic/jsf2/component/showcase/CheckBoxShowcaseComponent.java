package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class CheckBoxShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

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

        this.appendString("tooltip", this.getTooltip(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);
        this.appendBoolean("disableDefaultStyleClasses", this.isDisableDefaultStyleClasses(), sb);

        if (this.isBootstrap()) {
            this.appendString("componentStyleClass", "form-group", sb);
            this.appendString("inputStyleClass", "form-control", sb);
        }

        this.appendBoolean("rendered", this.isRendered(), sb, true);

        this.createAjaxXhtml(sb, "change");

        if (getFacetText() != null && !"".equals(getFacetText())) {
            sb.append("            " + "<f:facet name=\"input-container\">\n");
            sb.append("            " + "    " + getFacetText() + "\n");
            sb.append("            " + "</f:facet>\n");
        }

        sb.append("        </l:checkBox>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    public void addCss(StringBuilder sb) {
        if (this.isBootstrap()) {
            sb.append("\n\n.form-group input[type=checkbox] {\n");
            sb.append("    width: 14px; /* fixes checkbox position */\n");
            sb.append("    height: 14px; /* fixes checkbox position */\n");
            sb.append("}");
        }
    }

    protected String getEmptyDistanceString() {
        return "                    ";
    }
}
