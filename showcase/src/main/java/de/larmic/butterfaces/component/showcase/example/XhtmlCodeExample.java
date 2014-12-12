package de.larmic.butterfaces.component.showcase.example;

/**
 * Created by larmic on 12.12.14.
 */
public class XhtmlCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();

    private final boolean useFontAwesome;

    public XhtmlCodeExample(final boolean useFontAwesome) {
        this("xhtml", "xhtml", useFontAwesome);
    }

    public XhtmlCodeExample(final String tabName, final String tabId, final boolean useFontAwesome) {
        super(tabName, tabId);
        this.useFontAwesome = useFontAwesome;
    }

    @Override
    public String getPrettyPrintLang() {
        return "lang-html";
    }

    public StringBuilder appendInnerContent(final String content) {
        return innerContent.append(content).append("\n");
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html> \n");
        stringBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" \n");
        stringBuilder.append("      xmlns:h=\"http://java.sun.com/jsf/html\" \n");
        stringBuilder.append("      xmlns:f=\"http://java.sun.com/jsf/core\" \n");
        stringBuilder.append("      xmlns:b=\"http://butterfaces.larmic.de/components\"> \n");
        if (useFontAwesome) {
            stringBuilder.append("<h:head> \n");
            stringBuilder.append("    <link href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css\"\n");
            stringBuilder.append("          rel=\"stylesheet\">\n");
            stringBuilder.append("</h:head> \n");
        } else {
            stringBuilder.append("<h:head /> \n");
        }
        stringBuilder.append("<body>");
        stringBuilder.append("\n");
        stringBuilder.append("    <form>");
        stringBuilder.append("\n");
        stringBuilder.append(innerContent.toString());
        stringBuilder.append("\n");
        stringBuilder.append("    </form> \n");
        stringBuilder.append("</body> \n");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }
}
