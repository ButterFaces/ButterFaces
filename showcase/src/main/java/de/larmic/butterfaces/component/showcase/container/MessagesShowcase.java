package de.larmic.butterfaces.component.showcase.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

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
      final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);
      xhtmlCodeExample.setWrappedByForm(false);

      xhtmlCodeExample.appendInnerContent("    <b:messages id=\"input\"");
      xhtmlCodeExample.appendInnerContent("               globalOnly=\"" + this.isGlobalOnly() + "\"");
      xhtmlCodeExample.appendInnerContent("               showDetail=\"" + this.isShowDetail() + "\"");
      xhtmlCodeExample.appendInnerContent("               rendered=\"" + this.isRendered() + "\" />");

      codeExamples.add(xhtmlCodeExample);
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

   private void addFacesMessage(FacesMessage message) {
      facesMessages.add(message);
      if (!facesMessages.isEmpty()) {
         FacesContext facesContext = FacesContext.getCurrentInstance();
         for (FacesMessage facesMessage : facesMessages) {
            facesContext.addMessage(null, facesMessage);
         }
      }
   }
}
