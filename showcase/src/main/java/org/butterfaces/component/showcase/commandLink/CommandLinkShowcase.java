package org.butterfaces.component.showcase.commandLink;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.commandLink.example.CommandLinkWebXmlExample;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkExampleType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkGlyphiconType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkRenderType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkStyleType;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.commandLink.example.CommandLinkWebXmlExample;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkExampleType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkGlyphiconType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkRenderType;
import org.butterfaces.component.showcase.commandLink.type.CommandLinkStyleType;
import org.butterfaces.util.StringUtils;

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
    private CommandLinkGlyphiconType glyphicon = CommandLinkGlyphiconType.BOOTSTRAP;
    private CommandLinkStyleType style = CommandLinkStyleType.BOOTSTRAP_BUTTON;
    private int clicks = 0;
    private boolean ajaxDisableLinkOnRequest = true;
    private boolean ajaxShowWaitingDotsOnRequest = true;
    private boolean ajaxHideGlyphiconOnRequest = false;
    private boolean ajaxDisableRenderRegionsOnRequest = true;
    private String ajaxProcessingText = "Processing";
    private String ajaxProcessingGlyphicon = "fa fa-refresh fa-spin";
    private CommandLinkRenderType render = CommandLinkRenderType.SECTIONS;
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
        final boolean useFontAwesome = this.getGlyphicon() == CommandLinkGlyphiconType.AWESOME;
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
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-outline-secondary\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink value=\"render (resetValues)\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-outline-secondary\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                   resetValues=\"true\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("\n        <h:commandLink value=\"render (reset values JSF2 default)\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-outline-secondary\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\"");
        xhtmlCodeExample.appendInnerContent("                   render=\"rerenderArea\"");
        xhtmlCodeExample.appendInnerContent("                   resetValues=\"true\"/>");
        xhtmlCodeExample.appendInnerContent("        </h:commandLink>");
        return xhtmlCodeExample;
    }

    private XhtmlCodeExample createXhtmlCodeExampleResetValues() {
        final boolean useFontAwesome = this.getGlyphicon() == CommandLinkGlyphiconType.AWESOME;
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(useFontAwesome);

        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                       value=\"" + this.getValue() + "\"");
        if (glyphicon != CommandLinkGlyphiconType.DEFAULT) {
            xhtmlCodeExample.appendInnerContent("                       glyphicon=\"" + this.glyphicon.value + "\"");
        }
        if (style != CommandLinkStyleType.LINK) {
            xhtmlCodeExample.appendInnerContent("                       styleClass=\"" + this.style.value + "\"");
        }
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

    public List<EnumTreeBoxWrapper> getGlyphicons() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final CommandLinkGlyphiconType type : CommandLinkGlyphiconType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getStyles() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final CommandLinkStyleType type : CommandLinkStyleType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getRenders() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final CommandLinkRenderType type : CommandLinkRenderType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getCommandLinkExamples() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final CommandLinkExampleType type : CommandLinkExampleType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSelectedGlyphicon() {
        return glyphicon.value;
    }

    public CommandLinkGlyphiconType getGlyphicon() {
        return glyphicon;
    }

    public void setGlyphicon(CommandLinkGlyphiconType glyphicon) {
        this.glyphicon = glyphicon;
    }

    public String getSelectedStyle() {
        return style.value;
    }

    public CommandLinkStyleType getStyle() {
        return style;
    }

    public void setStyle(CommandLinkStyleType style) {
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

    public String getSelectedRender() {
        return render.value;
    }

    public CommandLinkRenderType getRender() {
        return render;
    }

    public void setRender(CommandLinkRenderType render) {
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
