package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.type.PrettyPrintType;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class PrettyPrintShowcase extends AbstractCodeShowcase implements Serializable {

    private PrettyPrintType prettyPrintType = PrettyPrintType.HTML;

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);
        xhtmlCodeExample.setWrappedByForm(false);

        xhtmlCodeExample.appendInnerContent("\n        <b:prettyprint id=\"pretty\"");
        xhtmlCodeExample.appendInnerContent("                       language=\"" + getLanguage() + "\"");
        xhtmlCodeExample.appendInnerContent("                       rendered=\"" + isRendered() + "\">");
        if (prettyPrintType == PrettyPrintType.HTML) {
            xhtmlCodeExample.appendInnerContent("           #{myBean.htmlContent}");
        } else if (prettyPrintType == PrettyPrintType.JAVA) {
            xhtmlCodeExample.appendInnerContent("           #{myBean.javaContent}");
        } else {
            xhtmlCodeExample.appendInnerContent("           #{myBean.xmlContent}");
        }
        xhtmlCodeExample.appendInnerContent("        </b:prettyprint>");

        codeExamples.add(xhtmlCodeExample);
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
        html.append("      xmlns:h=\"http://xmlns.jcp.org/jsf/html\"\n");
        html.append("      xmlns:b=\"http://butterfaces.org/components\">\n");
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
