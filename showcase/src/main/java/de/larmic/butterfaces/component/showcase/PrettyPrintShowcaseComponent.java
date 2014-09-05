package de.larmic.butterfaces.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PrettyPrintShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

    @Override
    protected Object initValue() {
        return "value";
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    public String getXml() {
        return "<xml></xml>";
    }

    public String getHtmlExample() {
        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n");
        html.append("      xmlns:h=\"http://java.sun.com/jsf/html\"\n");
        html.append("      xmlns:l=\"http://butterfaces.larmic.de/components\">\n");
        html.append("<h:head />\n");
        html.append("<body>\n");
        html.append("   <form>\n");
        html.append("      <l:fieldset id=\"input\"\n");
        html.append("                  label=\"label\"\n");
        html.append("                  rendered=\"true\">\n");
        html.append("      </l:fieldset>\n");
        html.append("   </form>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <l:fieldset id=\"input\"\n");

        this.appendString("label", this.getLabel(), sb, !this.isRendered());
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </l:fieldset>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                    ";
    }
}
