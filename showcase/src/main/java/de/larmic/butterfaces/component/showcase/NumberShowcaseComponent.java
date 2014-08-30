package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class NumberShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

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

        sb.append("        <l:number id=\"input\"\n");
        sb.append("                  label=\"" + this.getLabel() + "\"\n");
        sb.append("                  value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);
        this.appendString("min", this.getMin(), sb);
        this.appendString("max", this.getMax(), sb);
        this.appendString("componentStyleClass", this.getComponentStyleClass(), sb);
        this.appendString("inoutStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("floating", this.isFloating(), sb);
        this.appendBoolean("autoFocus", this.isAutoFocus(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        this.createAjaxXhtml(sb, "keyup");

        sb.append("        </l:number>");

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
