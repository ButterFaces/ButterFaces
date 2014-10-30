package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class CheckBoxShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

    private String description = "some description";

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

        sb.append("        <b:checkBox id=\"input\"\n");
        sb.append("                    label=\"" + this.getLabel() + "\"\n");
        sb.append("                    value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);
        this.appendString("description", this.getDescription(), sb);
        this.appendString("componentStyleClass", this.getComponentStyleClass(), sb);
        this.appendString("inputStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        this.createAjaxXhtml(sb, "change");

        sb.append("        </b:checkBox>");

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected String getEmptyDistanceString() {
        return "                    ";
    }
}
