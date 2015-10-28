package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.resolver.ELResolver;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

/**
 * Created by larmic on 27.08.14.
 */
public class ReadonlyPartRenderer {

    public void renderReadonly(final HtmlInputComponent component, final ResponseWriter writer) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final boolean readonly = component.isReadonly();
        final Object value = component.getValue();

        if (readonly) {
            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, uiComponent);
            final StringBuilder sb = new StringBuilder("butter-component-value butter-component-value-readonly");
            if (component.isHideLabel()) {
                sb.append(" butter-component-value-hiddenLabel");
            }
            writer.writeAttribute("class", sb.toString(), null);
            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "butter-component-value-readonly-wrapper", "styleClass");
            writer.writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
            writer.endElement("span");
            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
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
            final String asString = converter.getAsString(FacesContext.getCurrentInstance(), component, value);
            return asString == null ? "-" : asString;
        }

        if (component instanceof HtmlCheckBox) {
            HtmlCheckBox checkBoxComponent = (HtmlCheckBox) component;
            final StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotEmpty(checkBoxComponent.getDescription())) {
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
                final UISelectItems uiSelectItems = (UISelectItems) child;
                final List items = (List) uiSelectItems.getValue();
                for (Object item : items) {
                    if (item instanceof SelectItem) {
                        final SelectItem selectItem = (SelectItem) item;
                        if (this.isMatchingLabel(selectItem, value)) {
                            return selectItem.getLabel();
                        }
                    } else {
                        // TODO check converter???
                        final FacesContext facesContext = FacesContext.getCurrentInstance();
                        final Object var = child.getAttributes().get("var");

                        if (var != null) {
                            final Object itemValue = ELResolver.resolve(facesContext, child, "itemValue", var.toString(), item);
                            final Object itemLabel = ELResolver.resolve(facesContext, child, "itemLabel", var.toString(), item);

                            if (itemValue != null && itemValue.toString().equals(value) && itemLabel != null) {
                                return itemLabel.toString();
                            }
                        }
                    }
                }
            } else if (child instanceof UISelectItem) {
                final UISelectItem item = (UISelectItem) child;

                if (this.isMatchingLabel(item, value)) {
                    return item.getItemLabel();
                }
            }
        }

        return String.valueOf(value);
    }

    private static Object executeExpressionInElContext (Application application, ELContext elContext, String expression) {
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, expression, Object.class);
        return exp.getValue(elContext);
    }

    public static void setValue2ValueExpression(final Object value, final String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();

        ValueExpression targetExpression =
                facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, expression, Object.class);
        targetExpression.setValue(elContext, value);
    }

    private boolean isMatchingLabel(final SelectItem item, final Object value) {
        return value.equals(item.getValue());
    }

    private boolean isMatchingLabel(final UISelectItem item, final Object value) {
        return value.equals(item.getValue());
    }

}
