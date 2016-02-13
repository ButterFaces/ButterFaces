package de.larmic.butterfaces.component.showcase.example;

/**
 * Created by larmic on 12.12.14.
 */
public class WebXmlCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();

    public WebXmlCodeExample(String tabName, String tabId) {
        super(tabName, tabId);
    }

    @Override
    public String getPrettyPrintLang() {
        return "lang-html";
    }

    public StringBuilder appendInnerContent(final String content) {
        return this.appendInnerContent(content, true);
    }

    public StringBuilder appendInnerContent(final String content, final boolean lineBreak) {
        if (lineBreak) {
            return innerContent.append(content).append("\n");
        } else {
            return innerContent.append(content);
        }
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
        stringBuilder.append("<web-app xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" \n");
        stringBuilder.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
        stringBuilder.append("         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee \n");
        stringBuilder.append("                             http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" \n");
        stringBuilder.append("         version=\"3.0\"> \n");
        stringBuilder.append(innerContent.toString());
        stringBuilder.append("</web-app>");

        return stringBuilder.toString();
    }
}
