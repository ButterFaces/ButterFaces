package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class LinkShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private String value = "click me";
    private int clicks = 0;

    public void increaseClick() {
        clicks++;
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <l:commandLink id=\"input\"\n");

        this.appendString("value", this.getValue(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </l:commandLink>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                       ";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getClicks() {
        return clicks;
    }
}
