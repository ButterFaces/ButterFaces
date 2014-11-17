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
    private String style = "btn btn-primary";
    private int clicks = 0;
    private boolean ajaxDisableLinkOnRequest = true;
    private boolean ajaxShowWaitingDotsOnRequest = true;
    private String ajaxProcessingText = "Processing";

    public void increaseClick() {
        if (ajaxDisableLinkOnRequest) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.link,demo;\n\n");

        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        sb.append("public class MyBean implements Serializable {\n\n");
        sb.append("    private int clicks = 0;\n\n");
        sb.append("    public void increaseClick() {\n");
        if(ajaxDisableLinkOnRequest) {
            sb.append("        try {\n");
            sb.append("            Thread.sleep(2000);\n");
            sb.append("        } catch (InterruptedException e) {\n");
            sb.append("            // this error is not ok...\n");
            sb.append("        }\n");
        }
        sb.append("        clicks++\n");
        sb.append("    }\n\n");
        sb.append("    public int getClicks() {\n");
        sb.append("        return clicks;\n");
        sb.append("    }\n\n");
        sb.append("}");
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
        this.appendString("ajaxDisableLinkOnRequest", this.isAjaxDisableLinkOnRequest() + "", sb);
        this.appendString("ajaxShowWaitingDotsOnRequest", this.isAjaxShowWaitingDotsOnRequest() + "", sb);

        if (!"Processing".equals(this.getAjaxProcessingText())) {
            this.appendString("ajaxProcessingText", this.getAjaxProcessingText() + "", sb);
        }

        sb.append(getEmptyDistanceString() + "action=#{myBean.increaseClick}\n");

        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("            <f:ajax render=\"clicks\" />\n");
        sb.append("        </b:commandLink>\n\n");
        sb.append("        <hr />\n\n");
        sb.append("        <h:panelGroup id=\"clicks\" layout=\"block\">\n");
        sb.append("            #{myBean.clicks} clicks\n");
        sb.append("        </h:panelGroup >");

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

    public boolean isAjaxDisableLinkOnRequest() {
        return ajaxDisableLinkOnRequest;
    }

    public void setAjaxDisableLinkOnRequest(boolean ajaxDisableLinkOnRequest) {
        this.ajaxDisableLinkOnRequest = ajaxDisableLinkOnRequest;
    }

    public boolean isAjaxShowWaitingDotsOnRequest() {
        return ajaxShowWaitingDotsOnRequest;
    }

    public void setAjaxShowWaitingDotsOnRequest(boolean ajaxShowWaitingDotsOnRequest) {
        this.ajaxShowWaitingDotsOnRequest = ajaxShowWaitingDotsOnRequest;
    }

    public String getAjaxProcessingText() {
        return ajaxProcessingText;
    }

    public void setAjaxProcessingText(String ajaxProcessingText) {
        this.ajaxProcessingText = ajaxProcessingText;
    }
}
