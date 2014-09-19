package de.larmic.butterfaces.component.showcase;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class LinkShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private String value = "click me";
    private String glyphicon = "glyphicon glyphicon-thumbs-up glyphicon-lg";
    private String style = null;
    private int clicks = 0;

    public void increaseClick() {
        clicks++;
    }

    public List<SelectItem> getGlyphicons() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem(null, "No glyphicon"));
        items.add(new SelectItem("glyphicon glyphicon-thumbs-up glyphicon-lg", "Bootstrap example"));
        items.add(new SelectItem("fa fa-language fa-lg", "Font-Awesome example"));

        return items;
    }

    public List<SelectItem> getStyles() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem(null, "default link"));
        items.add(new SelectItem("btn btn-primary", "Bootstrap button"));

        return items;
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        if (this.getGlyphicon() != null && this.getGlyphicon().contains("fa")) {
            this.addXhtmlStart(sb, "<h:head>\n    <link href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css\"\n          rel=\"stylesheet\">\n</h:head>");
        } else {
            this.addXhtmlStart(sb);
        }

        sb.append("        <b:commandLink id=\"input\"\n");

        this.appendString("value", this.getValue(), sb);
        this.appendString("glyphicon", this.getGlyphicon(), sb);
        this.appendString("styleClass", this.getStyle(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </b:commandLink>");

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

    public String getGlyphicon() {
        return glyphicon;
    }

    public void setGlyphicon(String glyphicon) {
        this.glyphicon = glyphicon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getClicks() {
        return clicks;
    }
}
