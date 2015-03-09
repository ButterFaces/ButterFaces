package de.larmic.butterfaces.component.partrenderer;

import java.io.IOException;
import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;

/**
 * Created by larmic on 27.08.14.
 */
public class ReadonlyPartRenderer {

    public void renderReadonly(final HtmlInputComponent component, final ResponseWriter responseWriter) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final boolean readonly = component.isReadonly();
        final Object value = component.getValue();

        if (readonly) {
            responseWriter.startElement("div", uiComponent);
            final StringBuilder sb = new StringBuilder("butter-component-value butter-component-value-readonly");
            if (component.isHideLabel()) {
                sb.append(" butter-component-value-hiddenLabel");
            }
            responseWriter.writeAttribute("class", sb.toString(), null);
            responseWriter
                  .writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
            responseWriter.endElement("div");
        }
    }

    /**
     * Should return value string for the readonly view mode. Can be overridden
     * for custom components.
     */
    private String getReadonlyDisplayValue(final Object value, final UIInput component, final Converter converter) {
        if (value == null || "".equals(value)) {
            return "-";
        } else if (converter != null) {
            return converter.getAsString(FacesContext.getCurrentInstance(), component, value);
        }

        if (component instanceof HtmlCheckBox) {
            HtmlCheckBox checkBoxComponent = (HtmlCheckBox) component;
            final StringBuilder sb = new StringBuilder();
            if(StringUtils.isNotEmpty(checkBoxComponent.getDescription())){
                sb.append(checkBoxComponent.getDescription()).append(": ");
            }
            sb.append((Boolean) value ? "ja" : "nein");
            return sb.toString();
        }

        if (component instanceof HtmlComboBox) {
            return this.getReadableValueFrom((HtmlComboBox) component, value);
        }

        return String.valueOf(value);
    }

    private String getReadableValueFrom(final HtmlComboBox comboBox, final Object value) {
        for (final UIComponent child : comboBox.getChildren()) {
            if (child instanceof UISelectItems) {
                final ArrayList<SelectItem> items = (ArrayList<SelectItem>) ((UISelectItems) child).getValue();

                for (final SelectItem item : items) {
                    if (this.isMatchingLabel(item, value)) {
                        return item.getLabel();
                    }
                }
            }
            if (child instanceof UISelectItem) {
                final UISelectItem item = (UISelectItem) child;

                if (this.isMatchingLabel(item, value)) {
                    return item.getItemLabel();
                }
            }
        }

        return String.valueOf(value);
    }

    private boolean isMatchingLabel(final SelectItem item, final Object value) {
        return value.equals(item.getValue());
    }

    private boolean isMatchingLabel(final UISelectItem item, final Object value) {
        return value.equals(item.getValue());
    }

}
