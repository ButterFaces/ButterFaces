package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.type.PrettyPrintType;

import javax.faces.view.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class PrettyPrintShowcaseSingleCodeComponent extends AbstractInputShowcaseSingleCodeComponent implements Serializable {

    private PrettyPrintType prettyPrintType = PrettyPrintType.HTML;

    @Override
    protected Object initValue() {
        return "value";
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    public String getContent() {
        switch (prettyPrintType) {
            case JAVA:
                return getJavaExample();
            case XML:
                return getXmlExample();
            default:
                return getHtmlExample();
        }
    }

    public String getLanguage() {
        switch (prettyPrintType) {
            case JAVA:
                return "lang-java";
            case XML:
                return "lang-xml";
            default:
                return "lang-html";
        }
    }

    public List<SelectItem> getPrettyPrintTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final PrettyPrintType type : PrettyPrintType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <b:prettyprint id=\"input\"\n");

        this.appendString("language", this.getLanguage(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);
        if (prettyPrintType == PrettyPrintType.HTML) {
            sb.append("           #{myBean.htmlContent}\n");
        } else if (prettyPrintType == PrettyPrintType.JAVA) {
            sb.append("           #{myBean.javaContent}\n");
        } else {
            sb.append("           #{myBean.xmlContent}\n");
        }

        sb.append("        </b:prettyprint>");

        this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                       ";
    }

    private String getJavaExample() {
        final StringBuilder java = new StringBuilder();

        java.append("public class HelloWorld {\n\n");
        java.append("    public static void main(String[ ] args) {\n");
        java.append("        System.out.println(\"Hello World!\");\n");
        java.append("    }\n\n");
        java.append("}\n");

        return java.toString();
    }

    private String getXmlExample() {
        final StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
        xml.append("<root>\n");
        xml.append("    <title>Hello World!</title>\n");
        xml.append("</root>\n");

        return xml.toString();
    }

    private String getHtmlExample() {
        final StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n");
        html.append("      xmlns:h=\"http://java.sun.com/jsf/html\"\n");
        html.append("      xmlns:b=\"http://butterfaces.larmic.de/components\">\n");
        html.append("<h:head />\n");
        html.append("<body>\n");
        html.append("   <form>\n");
        html.append("      Hello World!\n");
        html.append("   </form>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    public PrettyPrintType getPrettyPrintType() {
        return prettyPrintType;
    }

    public void setPrettyPrintType(PrettyPrintType prettyPrintType) {
        this.prettyPrintType = prettyPrintType;
    }
}
