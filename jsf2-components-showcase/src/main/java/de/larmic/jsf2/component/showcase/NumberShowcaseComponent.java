package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class NumberShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private String placeholder;
    private String min;
    private String max;
    private boolean autoFocus;

    @Override
    protected Object initValue() {
        return "3";
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <l:number id=\"input\"\n");
        sb.append("                  label=\"" + this.getLabel() + "\"\n");
        sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);
        this.appendString("min", this.getMin(), sb);
        this.appendString("max", this.getMax(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);
        this.appendBoolean("autoFocus", this.isAutoFocus(), sb);
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

        sb.append("        </l:number>");

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

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
