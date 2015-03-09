package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class NumberShowcaseSingleCodeComponent extends AbstractInputShowcaseSingleCodeComponent implements Serializable {

    private String placeholder = DEFAULT_NUMBER_PLACEHOLDER;
    private String min;
    private String max;
    private boolean autoFocus;

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

        sb.append("        <b:number id=\"input\"\n");
        sb.append("                  label=\"" + this.getLabel() + "\"\n");
        sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);
        this.appendString("min", this.getMin(), sb);
        this.appendString("max", this.getMax(), sb);
        this.appendString("styleClass", this.getStyleClass(), sb);
        this.appendString("inputStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("autoFocus", this.isAutoFocus(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        this.createAjaxXhtml(sb, "change");

        sb.append("        </b:number>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected void addCss(StringBuilder sb) {
        if (!StringUtils.isEmpty(this.getStyleClass())) {
            sb
                  .append(".demo-big-label .butter-component-label {\n")
                  .append("    width: 250px;\n")
                  .append("}\n")
                  .append("\n")
                  .append(".demo-big-label .butter-component-value {\n")
                  .append("    width: calc(100% - 250px);\n")
                  .append("}");
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
