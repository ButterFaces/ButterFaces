package de.larmic.butterfaces.component.showcase.commandLink;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.commandLink.example.CommandLinkWebXmlExample;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class CommandLinkShowcase extends AbstractCodeShowcase implements Serializable {

    private String resetValue = "";
    private String value = "click me";
    private String glyphicon = "glyphicon glyphicon-thumbs-up glyphicon-lg";
    private String style = "btn btn-primary";
    private int clicks = 0;
    private boolean ajaxDisableLinkOnRequest = true;
    private boolean ajaxShowWaitingDotsOnRequest = true;
    private boolean ajaxHideGlyphiconOnRequest = false;
    private boolean ajaxDisableRenderRegionsOnRequest = true;
    private String ajaxProcessingText = "Processing";
    private String ajaxProcessingGlyphicon = "fa fa-refresh fa-spin";
    private String render = "clicks disabledOnRequest otherDisabledOnRequest";
    private CommandLinkExampleType commandLinkExampleType = CommandLinkExampleType.AJAX;

    public void increaseClick() {
        System.out.println("action called");
        clicks++;
    }

    public void increaseClickWithDelay() {
        if (ajaxDisableLinkOnRequest) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        increaseClick();
    }

    public void actionListener(javax.faces.event.ActionEvent event) {
        System.out.println("actionlistener called " + event.toString());
    }

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final boolean resetValuesExample = commandLinkExampleType != CommandLinkExampleType.RESET_VALUES;
        final XhtmlCodeExample xhtmlCodeExample = resetValuesExample ? createXhtmlCodeExampleResetValues() : createXhtmlCodeExample();
        final JavaCodeExample javaCodeExample = resetValuesExample ? createJavaCodeExampleResetValues() : createJavaCodeExample();

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(javaCodeExample);
        codeExamples.add(new CommandLinkWebXmlExample());
    }

    private JavaCodeExample createJavaCodeExample() {
        final JavaCodeExample javaCodeExample = new JavaCodeExample("MyBean.java", "mybean", "command.link.demo", "MyBean", true);

        javaCodeExample.appendInnerContent("    private String value = \"\";\n");
        javaCodeExample.appendInnerContent("    // getter +  setter");

        return javaCodeExample;
    }

    private JavaCodeExample createJavaCodeExampleResetValues() {
        final JavaCodeExample javaCodeExample = new JavaCodeExample("MyBean.java", "mybean", "command.link.demo", "MyBean", true);

        javaCodeExample.appendInnerContent("    private int clicks = 0;\n");

        javaCodeExample.appendInnerContent("    public void increaseClick() {");
        if (ajaxDisableLinkOnRequest && commandLinkExampleType == CommandLinkExampleType.AJAX) {
            javaCodeExample.appendInnerContent("        try {");
            javaCodeExample.appendInnerContent("            Thread.sleep(2000);");
            javaCodeExample.appendInnerContent("        } catch (InterruptedException e) {");
            javaCodeExample.appendInnerContent("            // this error is not ok...");
            javaCodeExample.appendInnerContent("        }");
        }
        javaCodeExample.appendInnerContent("        clicks++");
        javaCodeExample.appendInnerContent("    }\n");
        javaCodeExample.appendInnerContent("    public int getClicks() {");
        javaCodeExample.appendInnerContent("        return clicks;");
        javaCodeExample.appendInnerContent("    }");

        return javaCodeExample;
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final boolean useFontAwesome = this.getGlyphicon() != null && this.getGlyphicon().contains("fa");
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(useFontAwesome);

        xhtmlCodeExample.appendInnerContent("\n        <div class=\"row\">");
        xhtmlCodeExample.appendInnerContent("           <h:panelGroup id=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                         styleClass=\"col-md-10\">");
        xhtmlCodeExample.appendInnerContent("              <b:text label=\"Not null value\"");
        xhtmlCodeExample.appendInnerContent("                      value=\"#{myBean.value}\"");
        xhtmlCodeExample.appendInnerContent("                      required=\"true\">");
        xhtmlCodeExample.appendInnerContent("           </h:panelGroup>");
        xhtmlCodeExample.appendInnerContent("\n           <div class=\"col-md-2\">");
        xhtmlCodeExample.appendInnerContent("              <b:commandLink value=\"Submit\"");
        xhtmlCodeExample.appendInnerContent("                             styleClass=\"btn btn-success\">");
        xhtmlCodeExample.appendInnerContent("                 <f:ajax execute=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                         render=\"rerenderArea\"/>");
        xhtmlCodeExample.appendInnerContent("              </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("           </div>");
        xhtmlCodeExample.appendInnerContent("        </div>");

        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink value=\"render (no resetValues)\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-default\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink value=\"render (resetValues)\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-default\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                   resetValues=\"true\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("\n        <h:commandLink value=\"render (reset values JSF2 default)\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-default\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                   resetValues=\"true\"/>");
        xhtmlCodeExample.appendInnerContent("        </h:commandLink>");
        return xhtmlCodeExample;
    }

    private XhtmlCodeExample createXhtmlCodeExampleResetValues() {
        final boolean useFontAwesome = this.getGlyphicon() != null && this.getGlyphicon().contains("fa");
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(useFontAwesome);

        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                       value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                       glyphicon=\"" + this.getGlyphicon() + "\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"" + this.getStyle() + "\"");
        xhtmlCodeExample.appendInnerContent("                       ajaxDisableLinkOnRequest=\"" + this.isAjaxDisableLinkOnRequest() + "\"");
        xhtmlCodeExample.appendInnerContent("                       ajaxShowWaitingDotsOnRequest=\"" + this.isAjaxShowWaitingDotsOnRequest() + "\"");
        xhtmlCodeExample.appendInnerContent("                       ajaxHideGlyphiconOnRequest=\"" + this.isAjaxHideGlyphiconOnRequest() + "\"");
        xhtmlCodeExample.appendInnerContent("                       ajaxDisableRenderRegionsOnRequest=\"" + this.isAjaxDisableRenderRegionsOnRequest() + "\"");

        if (!"Processing".equals(this.getAjaxProcessingText())) {
            xhtmlCodeExample.appendInnerContent("                       ajaxProcessingText=\"" + this.getAjaxProcessingText() + "\"");
        }
        if (StringUtils.isNotEmpty(ajaxProcessingGlyphicon)) {
            xhtmlCodeExample.appendInnerContent("                       ajaxProcessingGlyphicon=\"" + ajaxProcessingGlyphicon + "\"");
        }

        xhtmlCodeExample.appendInnerContent("                       action=\"#{myBean.increaseClick}\"");
        xhtmlCodeExample.appendInnerContent("                       rendered=\"" + this.isRendered() + "\">");

        if (commandLinkExampleType == CommandLinkExampleType.AJAX) {
            xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"" + render + "\" />");
        }
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("\n        <hr />");
        xhtmlCodeExample.appendInnerContent("\n        <h:panelGroup id=\"clicks\" layout=\"block\">");
        xhtmlCodeExample.appendInnerContent("            #{myBean.clicks} clicks");
        xhtmlCodeExample.appendInnerContent("        </h:panelGroup>");
        return xhtmlCodeExample;
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

    public List<SelectItem> getRenders() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("clicks disabledOnRequest otherDisabledOnRequest", "some sections"));
        items.add(new SelectItem("@form", "@form"));
        items.add(new SelectItem("@this", "@this"));
        items.add(new SelectItem("@none", "@none"));

        return items;
    }

    public List<SelectItem> getCommandLinkExamples() {
        final List<SelectItem> items = new ArrayList<>();

        for (final CommandLinkExampleType type : CommandLinkExampleType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
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

    public boolean isAjaxHideGlyphiconOnRequest() {
        return ajaxHideGlyphiconOnRequest;
    }

    public void setAjaxHideGlyphiconOnRequest(boolean ajaxHideGlyphiconOnRequest) {
        this.ajaxHideGlyphiconOnRequest = ajaxHideGlyphiconOnRequest;
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        return ajaxDisableRenderRegionsOnRequest;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.ajaxDisableRenderRegionsOnRequest = ajaxDisableRenderRegionsOnRequest;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public CommandLinkExampleType getCommandLinkExampleType() {
        return commandLinkExampleType;
    }

    public void setCommandLinkExampleType(CommandLinkExampleType commandLinkExampleType) {
        this.commandLinkExampleType = commandLinkExampleType;
    }

    public String getAjaxProcessingGlyphicon() {
        return ajaxProcessingGlyphicon;
    }

    public void setAjaxProcessingGlyphicon(String ajaxProcessingGlyphicon) {
        this.ajaxProcessingGlyphicon = ajaxProcessingGlyphicon;
    }

    public String getResetValue() {
        return resetValue;
    }

    public void setResetValue(String resetValue) {
        this.resetValue = resetValue;
    }
}
