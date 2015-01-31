package de.larmic.butterfaces.component.partrenderer;

import java.io.IOException;

import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;

import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;

public class FilterableSelectPartRenderer {

   public void renderFilterable(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
      final UIInput uiComponent = (UIInput) component;
      final String outerComponentId = component.getClientId();

      if (isFilterableNecessary(component)) {
         RenderUtils.renderJQueryPluginCall(outerComponentId, "butterFilterableSelect()", writer, uiComponent);
      }
   }

   private boolean isFilterableNecessary(final HtmlInputComponent component) {
      return Boolean.TRUE.equals(((HtmlComboBox) component).isFilterable());
   }
}
