package de.larmic.jsf2.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class FieldSetShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

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

        sb.append("        <l:fieldset id=\"input\"\n");
        sb.append("                    label=\"" + this.getLabel() + "\"\n");

        if (this.isBootstrap()) {
            this.appendString("componentStyleClass", "form-group", sb);
            this.appendString("inputStyleClass", "form-control", sb);
        }

        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </l:fieldset>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }
}
