package org.butterfaces.component.renderkit.html_basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.html.message.HtmlMessages;
import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

/**
 * The renderer for the messages component. Actual only global messages will be rendered: Inspired by
 * com.sun.faces.renderkit.html_basic.MessagesRenderer from Mojarra
 */
@FacesRenderer(
      componentFamily = HtmlMessages.COMPONENT_FAMILY,
      rendererType = HtmlMessages.RENDERER_TYPE
)
public class MessagesRenderer extends HtmlBasicRenderer {

   private static final String ELEMENT_UL = "ul";
   private static final String ELEMENT_LI = "li";

   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
      final HtmlMessages messagesComponent = (HtmlMessages) component;

      if (messagesComponent.isRendered()) {
         final ResponseWriter writer = context.getResponseWriter();

         writer.startElement("div", component);
         writeIdAttributeIfNecessary(context, writer, component);

         StringBuffer styleClass = new StringBuffer();
         styleClass.append("butter-component-messages");
         if (messagesComponent.getStyleClass() != null) {
            styleClass.append(" ").append(messagesComponent.getStyleClass());
         }
         writer.writeAttribute(ATTRIBUTE_CLASS, styleClass.toString(), null);

         if (messagesComponent.getStyle() != null) {
            writer.writeAttribute(ATTRIBUTE_STYLE, messagesComponent.getStyle(), null);
         }

         final List<FacesMessage> messages;
         if (StringUtils.isEmpty(messagesComponent.getFor())) {
            messages = getMessages(context, messagesComponent.isGlobalOnly());
         } else {
            messages = getMessagesForId(context, messagesComponent.getFor());
         }

         final List<FacesMessage> errorMessages =
               filterMessages(messages, FacesMessage.SEVERITY_ERROR, FacesMessage.SEVERITY_FATAL);
         final List<FacesMessage> warnMessages = filterMessages(messages, FacesMessage.SEVERITY_WARN);
         final List<FacesMessage> infoMessages = filterMessages(messages, FacesMessage.SEVERITY_INFO);

         renderMessageList(errorMessages, "alert alert-danger", writer, messagesComponent);
         renderMessageList(warnMessages, "alert alert-warning", writer, messagesComponent);
         renderMessageList(infoMessages, "alert alert-info", writer, messagesComponent);

         writer.endElement("div");
      }

      super.encodeEnd(context, component);
   }

   private List<FacesMessage> filterMessages(List<FacesMessage> globalMessages, FacesMessage.Severity... severities) {
      final List<FacesMessage.Severity> severityList = Arrays.asList(severities);
      List<FacesMessage> filteredMessages = new ArrayList<>();
      for (FacesMessage message : globalMessages) {
         if (severityList.contains(message.getSeverity())) {
            filteredMessages.add(message);
         }
      }
      return filteredMessages;
   }

   private void renderMessageList(List<FacesMessage> messages, String containerStyleClass, ResponseWriter writer,
                                  HtmlMessages component) throws IOException {
      if (!messages.isEmpty()) {
         writer.startElement(ELEMENT_DIV, component);
         writer.writeAttribute(ATTRIBUTE_CLASS, containerStyleClass, null);
         writer.startElement(ELEMENT_UL, component);

         for (FacesMessage facesMessage : messages) {
            writer.startElement(ELEMENT_LI, component);

            renderMessagePart(writer, component, "summary", facesMessage.getSummary());
            if (facesMessage.getDetail() != null && component.isShowDetail()) {
               renderMessagePart(writer, component, "detail", facesMessage.getDetail());
            }

            writer.endElement(ELEMENT_LI);
         }

         writer.endElement(ELEMENT_UL);
         writer.endElement(ELEMENT_DIV);
      }
   }

   private void renderMessagePart(ResponseWriter writer, HtmlMessages component, String styleClass, String value)
         throws IOException {
      writer.startElement(ELEMENT_SPAN, component);
      writer.writeAttribute(ATTRIBUTE_CLASS, styleClass, null);
      writer.writeText(value, component, null);
      writer.endElement(ELEMENT_SPAN);
   }

   private List<FacesMessage> getMessages(FacesContext context, boolean globalOnly) {
      final List<FacesMessage> messages = new ArrayList<>();
      final Iterator messageIter;
      if (globalOnly) {
         messageIter = context.getMessages(null);
      } else {
         messageIter = context.getMessages();
      }
      while (messageIter.hasNext()) {
         messages.add((FacesMessage) messageIter.next());
      }
      return messages;
   }

   private List<FacesMessage> getMessagesForId(FacesContext context, String componentId) {
      final List<FacesMessage> messages = new ArrayList<>();
      final Iterator messageIter = context.getMessages(componentId);
      while (messageIter.hasNext()) {
         messages.add((FacesMessage) messageIter.next());
      }
      return messages;
   }
}
