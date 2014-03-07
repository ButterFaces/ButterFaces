package de.larmic.jsf2.renderkit.html_basic;

import de.larmic.jsf2.component.html.HtmlCheckBox;
import de.larmic.jsf2.component.html.HtmlComboBox;
import de.larmic.jsf2.component.html.HtmlInputComponent;
import de.larmic.jsf2.component.html.HtmlTextArea;

import javax.faces.application.FacesMessage;
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
import java.util.Iterator;

/**
 * Renderer support classes provides methods used by custom component
 * converters. Class is non static because of allowing overriding by other
 * custom components.
 */
public class InputRendererSupport {

    private static final String FLOATING_STYLE_CLASS = "larmic-component-floating";
    private static final String NON_FLOATING_STYLE_CLASS = "larmic-component-non-floating";

    private static final String COMPONENT_MARKER_STYLE_CLASS = "larmic-component-marker";
    private static final String COMPONENT_STYLE_CLASS = "larmic-component";

    private static final String INPUT_CONTAINER_MARKER_STYLE_CLASS = "larmic-input-container-marker";
    private static final String INPUT_CONTAINER_STYLE_CLASS = "larmic-input-container";

    private static final String LABEL_MARKER_STYLE_CLASS = "larmic-component-label-marker";
    private static final String LABEL_STYLE_CLASS = "larmic-component-label";

    private static final String INPUT_COMPONENT_MARKER = "larmic-input-component-marker";

    private static final String REQUIRED_SPAN_CLASS = "larmic-component-required";
    private static final String COMPONENT_INVALID_STYLE_CLASS = "larmic-component-invalid";
    private static final String INVALID_STYLE_CLASS = "larmic-component-input-invalid";
    private static final String TOOLTIP_CLASS = "larmic-component-tooltip";
    private static final String TOOLTIP_LABEL_CLASS = "larmic-component-label-tooltip";
    private static final String ERROR_MESSAGE_CLASS = "larmic-component-error-message";
    private static final String TEXT_AREA_MAXLENGTH_COUNTER_CLASS = "larmic-component-textarea-maxlength-counter";

    private static final String OUTERDIV_POSTFIX = "_outerComponentDiv";
    private static final String TOOLTIP_DIV_CLIENT_ID_POSTFIX = "_tooltip";

    /**
     * Render outer div and label (if needed) and initializes input component.
     * <p/>
     * NOTE: encodeBegin of super implementation should be called first.
     */
    public void encodeBegin(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        final boolean readonly = component.isReadonly();
        final boolean required = component.isRequired();
        final boolean floating = component.getFloating();
        final boolean disableDefaultStyleClasses = component.getDisableDefaultStyleClasses();
        final boolean valid = component.isValid();
        final String label = component.getLabel();
        final Object value = component.getValue();

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", uiComponent);
        this.initOuterDiv(component.getClientId(), component.getComponentStyleClass(), floating,
                disableDefaultStyleClasses, valid, writer);

        this.writeLabelIfNecessary(component, readonly, required, label, writer);

        if (readonly) {
            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "larmic-component-readonly", null);
            writer.writeText(this.getReadonlyDisplayValue(value, uiComponent, uiComponent.getConverter()), null);
            writer.endElement("span");
        }

        this.initInputComponent(uiComponent);

        writer.startElement("div", uiComponent);
        final String inputContainerStyleClass = disableDefaultStyleClasses ? null : INPUT_CONTAINER_STYLE_CLASS;
        writer.writeAttribute("class", this.concatStyles(INPUT_CONTAINER_MARKER_STYLE_CLASS, inputContainerStyleClass), null);
    }

    /**
     * Render tooltip and closes outer div.
     * <p/>
     * NOTE: getEndTextToRender of super implementation should be called first.
     */
    public void encodeEnd(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final UIInput uiComponent = (UIInput) component;
        final ResponseWriter writer = context.getResponseWriter();

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
            writer.startElement("div", uiComponent);
            writer.writeAttribute("id", uiComponent.getClientId() + TOOLTIP_DIV_CLIENT_ID_POSTFIX, null);
            writer.writeAttribute("class", TOOLTIP_CLASS, null);

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", "noteConnector", null);
            writer.endElement("div");

            if (tooltipNecessary) {
                writer.startElement("div", uiComponent);
                writer.writeAttribute("class", "noteContent", null);
                writer.writeText(component.getTooltip(), null);
                writer.endElement("div");
            }

            final Iterator<String> clientIdsWithMessages = context.getClientIdsWithMessages();

            while (clientIdsWithMessages.hasNext()) {
                final String clientIdWithMessages = clientIdsWithMessages.next();
                if (uiComponent.getClientId().equals(clientIdWithMessages)) {
                    final Iterator<FacesMessage> componentMessages = context.getMessages(clientIdWithMessages);

                    writer.startElement("div", uiComponent);
                    writer.writeAttribute("class", ERROR_MESSAGE_CLASS, null);
                    writer.startElement("ul", uiComponent);

                    while (componentMessages.hasNext()) {
                        writer.startElement("li", uiComponent);
                        writer.writeText(componentMessages.next().getDetail(), null);
                        writer.endElement("li");
                    }

                    writer.endElement("ul");
                    writer.endElement("div");
                }
            }

            writer.endElement("div");

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

        writer.endElement("div"); // .larmic-input-container

        jsCall.append("});");

        writer.startElement("script", uiComponent);
        writer.writeText(jsCall.toString(), null);
        writer.endElement("script");

        writer.endElement("div");
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

    protected void initOuterDiv(final String clientId, final String componentStyleClass, final boolean floating,
                                final boolean disableDefaultStyleClasses,
                                final boolean valid, final ResponseWriter writer) throws IOException {
        writer.writeAttribute("id", clientId + OUTERDIV_POSTFIX, null);

        final String floatingStyle = floating ? FLOATING_STYLE_CLASS : NON_FLOATING_STYLE_CLASS;
        final String validationClass = valid ? COMPONENT_INVALID_STYLE_CLASS : null;
        final String componentClass = disableDefaultStyleClasses ? null : COMPONENT_STYLE_CLASS;
        final String styleClass = this.concatStyles(COMPONENT_MARKER_STYLE_CLASS,
                componentClass, componentStyleClass, validationClass, floatingStyle);

        writer.writeAttribute("class", styleClass, null);
    }

    protected void writeLabelIfNecessary(final HtmlInputComponent component, final boolean readonly,
                                         final boolean required, final String label, final ResponseWriter writer) throws IOException {
        if (!this.isEmpty(label)) {
            final UIInput uiComponent = (UIInput) component;

            writer.startElement("label", uiComponent);
            if (!readonly) {
                writer.writeAttribute("for", uiComponent.getId(), null);
            }

            final String labelStyleClass = component.getDisableDefaultStyleClasses() ? null : LABEL_STYLE_CLASS;
            writer.writeAttribute("class", this.concatStyles(labelStyleClass, LABEL_MARKER_STYLE_CLASS,
                    TOOLTIP_LABEL_CLASS, component.getLabelStyleClass()), null);

            writer.startElement("abbr", uiComponent);
            if (this.isTooltipNecessary(component)) {
                writer.writeAttribute("title", component.getTooltip(), null);
                writer.writeAttribute("style", "cursor: help; word-wrap: breaking-word;", null);
            }
            writer.writeText(component.getLabel(), null);
            writer.endElement("abbr");

            this.writeRequiredSpanIfNecessary(component.getClientId(), readonly, required, writer);

            writer.endElement("label");
        }
    }

    protected void writeRequiredSpanIfNecessary(final String clientId, final boolean readonly, final boolean required,
                                                final ResponseWriter writer) throws IOException {
        if (required && !readonly) {
            writer.startElement("span", null);
            writer.writeAttribute("id", clientId + "_requiredLabel", null);
            writer.writeAttribute("class", REQUIRED_SPAN_CLASS, null);
            writer.writeText("*", null);
            writer.endElement("span");
        }
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
