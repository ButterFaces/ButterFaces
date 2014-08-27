package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;
import de.larmic.butterfaces.component.partrenderer.ComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.LabelPartRenderer;
import de.larmic.butterfaces.component.partrenderer.TooltipPartRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Renderer support classes provides methods used by custom component
 * converters. Class is non static because of allowing overriding by other
 * custom components.
 */
public class InputRendererSupport {

    private static final String INPUT_CONTAINER_MARKER_STYLE_CLASS = "larmic-input-container-marker";
    private static final String INPUT_CONTAINER_STYLE_CLASS = "larmic-input-container";
    private static final String INPUT_CONTAINER_FACET_MARKER_STYLE_CLASS = "larmic-input-container-facet-marker";
    private static final String INPUT_CONTAINER_FACET_NAME = "input-container";

    private static final String INPUT_COMPONENT_MARKER = "larmic-input-component-marker";

    private static final String INVALID_STYLE_CLASS = "larmic-component-input-invalid";
    private static final String TEXT_AREA_MAXLENGTH_COUNTER_CLASS = "larmic-component-textarea-maxlength-counter";

    private static final String OUTERDIV_POSTFIX = "_outerComponentDiv";

    /**
     * Render outer div and label (if needed) and initializes input component.
     * <p/>
     * NOTE: encodeBegin of super implementation should be called first.
     */
    public void encodeBegin(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final boolean readonly = component.isReadonly();
        final boolean disableDefaultStyleClasses = component.getDisableDefaultStyleClasses();
        final Object value = component.getValue();

        final ResponseWriter writer = context.getResponseWriter();

        new ComponentWrapperPartRenderer().renderComponentBegin(component, writer);
        new LabelPartRenderer().renderLabel(component, context.getResponseWriter());

        this.initInputComponent(uiComponent);

        writer.startElement("div", uiComponent);
        final String inputContainerStyleClass = disableDefaultStyleClasses ? null : INPUT_CONTAINER_STYLE_CLASS;
        writer.writeAttribute("class", this.concatStyles(INPUT_CONTAINER_MARKER_STYLE_CLASS, inputContainerStyleClass), null);

        if (readonly) {
            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "larmic-component-readonly", null);
            writer.writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
            writer.endElement("span");
        }
    }

    /**
     * Render tooltip and closes outer div.
     * <p/>
     * NOTE: getEndTextToRender of super implementation should be called first.
     */
    public void encodeEnd(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final ResponseWriter writer = context.getResponseWriter();

        new TooltipPartRenderer().renderTooltip(component, context.getResponseWriter(), context);

        final StringBuffer jsCall = new StringBuffer();
        jsCall.append("new ");
        if (uiComponent instanceof HtmlTextArea) {
            jsCall.append("TextareaComponentHandler");
        } else {
            jsCall.append("ComponentHandler");
        }
        final String outerComponentId = component.getClientId() + OUTERDIV_POSTFIX;
        jsCall.append("('").append(outerComponentId).append("', {");

        final boolean tooltipNecessary = this.isTooltipNecessary(component);

        if ((tooltipNecessary || !component.isValid()) && !component.isReadonly()) {
            jsCall.append("showTooltip:true");
        } else {
            jsCall.append("showTooltip:false");
        }

        if (uiComponent instanceof HtmlTextArea && ((HtmlTextArea) uiComponent).getMaxLength() != null) {
            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", TEXT_AREA_MAXLENGTH_COUNTER_CLASS, null);
            writer.endElement("div");

            jsCall.append(", maxLength:").append(((HtmlTextArea) uiComponent).getMaxLength().intValue());
        }

        final UIComponent inputContainerFacet = uiComponent.getFacet(INPUT_CONTAINER_FACET_NAME);
        if (inputContainerFacet != null) {
            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", INPUT_CONTAINER_FACET_MARKER_STYLE_CLASS, null);
            inputContainerFacet.encodeAll(context);
            writer.endElement("div");
        }

        writer.endElement("div"); // .larmic-input-container

        jsCall.append("});");

        writer.startElement("script", uiComponent);
        writer.writeText(jsCall.toString(), null);
        writer.endElement("script");

        new ComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    /**
     * Should return value string for the readonly view mode. Can be overridden
     * for custom components.
     */
    protected String getReadonlyDisplayValue(final Object value, final UIInput component, final Converter converter) {
        if (value == null || "".equals(value)) {
            return "-";
        } else if (converter != null) {
            return converter.getAsString(FacesContext.getCurrentInstance(), component, value);
        }

        if (component instanceof HtmlCheckBox) {
            return (Boolean) value ? "ja" : "nein";
        }

        if (component instanceof HtmlComboBox) {
            return this.getReadableValueFrom((HtmlComboBox) component, value);
        }

        return String.valueOf(value);
    }

    protected String getReadableValueFrom(final HtmlComboBox comboBox, final Object value) {
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

    protected void initInputComponent(final UIInput component) {
        final HtmlInputComponent htmlInputComponent = (HtmlInputComponent) component;
        final String styleClass = this.concatStyles(INPUT_COMPONENT_MARKER,
                htmlInputComponent.getInputStyleClass(),
                !component.isValid() ? INVALID_STYLE_CLASS : null);

        component.getAttributes().put("styleClass", styleClass);
    }


    protected String concatStyles(final String... styles) {
        final StringBuilder sb = new StringBuilder();

        for (final String style : styles) {
            if (style != null && !"".equals(style)) {
                sb.append(style);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    protected boolean isTooltipNecessary(final HtmlInputComponent component) {
        return !isEmpty(component.getTooltip());
    }

    private boolean isEmpty(final String value) {
        return !(value != null && !"".equals(value));
    }
}
