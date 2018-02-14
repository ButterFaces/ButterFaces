package org.butterfaces.component.showcase.example;

/**
 * Created by larmic on 12.12.14.
 */
public class XhtmlCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();

    private final boolean useFontAwesome;
    private final boolean usePassThrough;
    private boolean wrappedByForm = true;

    public XhtmlCodeExample(final boolean useFontAwesome) {
        this("xhtml", "xhtml", useFontAwesome, false);
    }

    public XhtmlCodeExample(final boolean useFontAwesome, final boolean usePassThrough) {
        this("xhtml", "xhtml", useFontAwesome, usePassThrough);
    }

    public XhtmlCodeExample(final String tabName, final String tabId, final boolean useFontAwesome, final boolean usePassThrough) {
        super(tabName, tabId);
        this.useFontAwesome = useFontAwesome;
        this.usePassThrough = usePassThrough;
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

        stringBuilder.append("<!DOCTYPE html> \n");
        stringBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" \n");
        stringBuilder.append("      xmlns:h=\"http://xmlns.jcp.org/jsf/html\" \n");
        stringBuilder.append("      xmlns:f=\"http://xmlns.jcp.org/jsf/core\" \n");
        if (usePassThrough) {
            stringBuilder.append("      xmlns:p=\"http://xmlns.jcp.org/jsf/passthrough\" \n");
        }
        stringBuilder.append("      xmlns:b=\"http://butterfaces.org/components\"> \n");
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
        if (wrappedByForm) {
            stringBuilder.append("    <h:form id=\"formId\">");
            stringBuilder.append("\n");
        }
        stringBuilder.append(innerContent.toString());
        stringBuilder.append("\n");
        if (wrappedByForm) {
            stringBuilder.append("    </h:form> \n");
        }
        stringBuilder.append("</body> \n");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }

    public void setWrappedByForm(boolean wrappedByForm) {
        this.wrappedByForm = wrappedByForm;
    }
}
