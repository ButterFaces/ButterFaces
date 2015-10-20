package de.larmic.butterfaces.component.html.message;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@FacesComponent(HtmlMessages.COMPONENT_TYPE)
public class HtmlMessages extends UIComponentBase {

   public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.messages";
   public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
   public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.MessagesRenderer";

   protected static final String PROPERTY_STYLE_CLASS = "styleClass";
   protected static final String PROPERTY_STYLE = "style";
   protected static final String SHOW_DETAIL = "showDetail";
   protected static final String GLOBAL_ONLY = "globalOnly";
   protected static final String FOR = "for";

   public HtmlMessages() {
      super();
      this.setRendererType(RENDERER_TYPE);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public String getStyleClass() {
      return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
   }

   public void setStyleClass(final String styleClass) {
      this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
   }

   public String getStyle() {
      return (String) this.getStateHelper().eval(PROPERTY_STYLE);
   }

   public void setStyle(final String style) {
      this.updateStateHelper(PROPERTY_STYLE, style);
   }

   public void setShowDetail(final boolean showDetail) {
      this.updateStateHelper(SHOW_DETAIL, showDetail);
   }

   public boolean isShowDetail() {
      return Boolean.valueOf(getStateHelper().eval(SHOW_DETAIL, Boolean.FALSE).toString());
   }

   public void setGlobalOnly(final boolean globalOnly) {
      this.updateStateHelper(GLOBAL_ONLY, globalOnly);
   }

   public boolean isGlobalOnly() {
      return Boolean.valueOf(getStateHelper().eval(GLOBAL_ONLY, Boolean.FALSE).toString());
   }

   public void setFor(final String styleClass) {
      this.updateStateHelper(FOR, styleClass);
   }

   public String getFor() {
      return (String) this.getStateHelper().eval(FOR);
   }

   private void updateStateHelper(final String propertyName, final Object value) {
      this.getStateHelper().put(propertyName, value);

      final ValueExpression ve = this.getValueExpression(propertyName);

      if (ve != null) {
         ve.setValue(this.getFacesContext().getELContext(), value);
      }
   }
}
