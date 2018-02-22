package org.butterfaces.component.showcase.container;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class MessagesShowcase extends AbstractCodeShowcase implements Serializable {

    private boolean showDetail;
    private boolean globalOnly;

    private String testText;

    private List<FacesMessage> facesMessages = new ArrayList<>();

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(createXhtmlCodeExample());
        codeExamples.add(createJavaCodeExample());
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);
        xhtmlCodeExample.setWrappedByForm(false);

        xhtmlCodeExample.appendInnerContent("    <h:form>");
        xhtmlCodeExample.appendInnerContent("        <b:messages globalOnly=\"" + this.isGlobalOnly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    showDetail=\"" + this.isShowDetail() + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\" />\n");

        xhtmlCodeExample.appendInnerContent("        <div class=\"btn-group\">");
        xhtmlCodeExample.appendInnerContent("            <b:commandLink value=\"add fatal message\"");
        xhtmlCodeExample.appendInnerContent("                           styleClass=\"btn btn-outline-secondary\"");
        xhtmlCodeExample.appendInnerContent("                           action=\"#{myBean.addFatalMessageToContext()}\">");
        xhtmlCodeExample.appendInnerContent("                <f:ajax execute=\"@this\" render=\"@form\"/>");
        xhtmlCodeExample.appendInnerContent("            </b:commandLink>");
        xhtmlCodeExample.appendInnerContent("        </div>");
        xhtmlCodeExample.appendInnerContent("    </h:form>");
        return xhtmlCodeExample;
    }

    private JavaCodeExample createJavaCodeExample() {
        final JavaCodeExample javaCodeExample = new JavaCodeExample("MyBean.java", "mybean", "tree.demo", "MyBean", true);

        javaCodeExample.addImport("import javax.faces.application.FacesMessage");
        javaCodeExample.addImport("import javax.faces.context.FacesContext");
        javaCodeExample.addImport("import javax.faces.view.ViewScoped");
        javaCodeExample.addImport("import javax.inject.Named");

        javaCodeExample.appendInnerContent("    public void addFatalMessageToContext() {");
        javaCodeExample.appendInnerContent("        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,");
        javaCodeExample.appendInnerContent("                                                  \"summary text...\",");
        javaCodeExample.appendInnerContent("                                                  \"detail text...\")");
        javaCodeExample.appendInnerContent("        FacesContext.getCurrentInstance().addMessage(null, facesMessage);");
        javaCodeExample.appendInnerContent("    }");

        return javaCodeExample;
    }

    public void addFatalMessageToContext() {
        addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_FATAL, "This is a fatal message summary text.",
                "This is a fatal message detail text."));
    }

    public void addErrorMessageToContext() {
        addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "This is an error message summary text.",
                "This is an error message detail text."));
    }

    public void addWarnMessageToContext() {
        addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "This is a warn message summary text.",
                "This is a warn message detail text."));
    }

    public void addInfoMessageToContext() {
        addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "This is an info message summary text.",
                "This is an info message detail text."));
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public boolean isGlobalOnly() {
        return globalOnly;
    }

    public void setGlobalOnly(boolean globalOnly) {
        this.globalOnly = globalOnly;
    }

    public void setTestText(String testText) {
        this.testText = testText;
    }

    public String getTestText() {
        return testText;
    }

    public void clearFacesMessages() {
        facesMessages.clear();
    }

    private void addFacesMessage(final FacesMessage message) {
        facesMessages.add(message);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        for (FacesMessage facesMessage : facesMessages) {
            facesContext.addMessage(null, facesMessage);
        }
    }
}
