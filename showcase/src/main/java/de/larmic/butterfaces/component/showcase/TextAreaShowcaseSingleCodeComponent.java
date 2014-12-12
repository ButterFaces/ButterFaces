package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TextAreaShowcaseSingleCodeComponent extends AbstractInputShowcaseSingleCodeComponent implements Serializable {

    private Integer maxLength;
    private Boolean expandable;

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

        sb.append("        <b:textArea id=\"input\"\n");
        sb.append("                    label=\"" + this.getLabel() + "\"\n");
        sb.append("                    value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("placeholder", this.getPlaceholder(), sb);
        this.appendString("styleClass", this.getStyleClass(), sb);
        this.appendString("inputStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);

        if (this.getMaxLength() != null) {
            sb.append("                    maxLength=\"" + this.getMaxLength() + "\"\n");
        }

        if (Boolean.TRUE.equals(this.getExpandable())) {
            sb.append("                    expandable=\"" + this.getExpandable() + "\"\n");
        }

        this.appendBoolean("rendered", this.isRendered(), sb, true);

        this.createAjaxXhtml(sb, "keyup");

        if (this.isValidation()) {
            sb.append("            <f:validateLength minimum=\"2\" maximum=\"10\"/>\n");
        }
        sb.append("        </b:textArea>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected void addCss(StringBuilder sb) {
        if (!StringUtils.isEmpty(this.getStyleClass())) {
            sb.append(".some-demo-class {\n");
            sb.append("    background-color: red;\n");
            sb.append("}");
        }
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

    public Boolean getExpandable() {
        return expandable;
    }

    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }
}
