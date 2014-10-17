package de.larmic.butterfaces.component.showcase;

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

        sb.append("        <b:fieldset id=\"input\"\n");

        this.appendString("label", this.getLabel(), sb);
        this.appendString("badgeText", this.getBadgeText(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);
        sb.append("           Lorem ipsum dolor sit amet, consectetuer ...\n");
        sb.append("        </b:fieldset>\n");
        sb.append("    Lorem ipsum dolor sit amet, consectetuer ...");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                    ";
    }
}
