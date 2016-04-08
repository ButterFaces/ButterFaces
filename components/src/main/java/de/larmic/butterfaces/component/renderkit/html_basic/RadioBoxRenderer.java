/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlRadioBox;
import de.larmic.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlRadioBox.COMPONENT_FAMILY, rendererType = HtmlRadioBox.RENDERER_TYPE)
public class RadioBoxRenderer extends AbstractHtmlTagRenderer<HtmlRadioBox> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        encodeBegin(context, component, "butter-component-radiobox");
    }

    @Override
    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        // TODO check parent method for missing attributes

        if (component instanceof HtmlRadioBox) {
            final HtmlRadioBox radioBox = (HtmlRadioBox) component;
            final String radioBoxClientId = radioBox.getClientId();
            final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());

            if (radioBox.getValues() instanceof Iterable) {
                int iterator = 0;
                for (java.lang.Object o : (Iterable) radioBox.getValues()) {
                    renderRadioBoxItem(writer, radioBox, radioBoxClientId, separatorChar, iterator, o);
                    iterator++;
                }
            }
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (!(component instanceof HtmlRadioBox)) {
            return;
        }

        final HtmlRadioBox radioBox = (HtmlRadioBox) component;

        if (!component.isRendered() || radioBox.isReadonly()) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        final Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        final Object item = findItemInValues((Iterable) radioBox.getValues(), requestMap.get(clientId));
        final String itemAsString = item != null ? convertItemToIdentifier(item) : null;
        setSubmittedValue(component, itemAsString);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        final HtmlRadioBox radioBox = (HtmlRadioBox) component;
        return submittedValue != null ? findItemInValues((Iterable) radioBox.getValues(), submittedValue.toString()) : null;
    }

    @Override
    protected void setSubmittedValue(UIComponent component, Object value) {
        super.setSubmittedValue(component, value != null ? value : "");
    }

    private void renderRadioBoxItem(ResponseWriter writer,
                                    HtmlRadioBox radioBox,
                                    String radioBoxClientId,
                                    char separatorChar,
                                    int itemCounter,
                                    Object listItem) throws IOException {
        final String radioItemClientId = radioBoxClientId + separatorChar + itemCounter;
        writer.startElement("div", radioBox);
        writer.writeAttribute("class", "radio", "class");
        writer.startElement("input", radioBox);
        writer.writeAttribute("id", radioItemClientId, "clientId");
        writer.writeAttribute("type", "radio", "input");
        writer.writeAttribute("name", radioBoxClientId, "name");
        writer.writeAttribute("value", convertItemToIdentifier(listItem), "value");

        if (isValueSelected(radioBox.getValue(), listItem)) {
            writer.writeAttribute("checked", true, "checked");
        }

        this.renderBooleanValue(radioBox, writer, "disabled");
        this.renderBooleanValue(radioBox, writer, "ismap");

        this.renderEventValue(radioBox, writer, "onblur", "blur");
        this.renderEventValue(radioBox, writer, "onclick", "click");
        this.renderEventValue(radioBox, writer, "ondblclick", "dblclick");
        this.renderEventValue(radioBox, writer, "onfocus", "focus");
        this.renderEventValue(radioBox, writer, "onkeydown", "keydown");
        this.renderEventValue(radioBox, writer, "onkeypress", "keypress");
        this.renderEventValue(radioBox, writer, "onkeyup", "keyup");
        this.renderEventValue(radioBox, writer, "onmousedown", "mousedown");
        this.renderEventValue(radioBox, writer, "onmousemove", "mousemove");
        this.renderEventValue(radioBox, writer, "onmouseout", "mouseout");
        this.renderEventValue(radioBox, writer, "onmouseover", "mouseover");
        this.renderEventValue(radioBox, writer, "onmouseup", "mouseup");
        this.renderEventValue(radioBox, writer, "onselect", "select");
        this.renderEventValue(radioBox, writer, "onchange", "change");

        writer.endElement("input");
        writer.startElement("label", radioBox);
        writer.writeAttribute("for", radioItemClientId, "for");
        writer.writeText(convertItemToText(listItem), radioBox, null);
        writer.endElement("label");
        writer.endElement("div");
    }

    private boolean isValueSelected(Object radioBoxValue, Object value) {
        return radioBoxValue != null && radioBoxValue.equals(value);
    }

    private Object findItemInValues(final Iterable values, final String identifier) {
        if (StringUtils.isEmpty(identifier) || values == null) {
            return null;
        }

        for (Object value : values) {
            if (convertItemToIdentifier(value).equals(identifier)) {
                return value;
            }
        }

        return null;
    }

    private String convertItemToIdentifier(final Object listItem) {
        // TODO maybe it is better to use hashcode
        return convertItemToText(listItem);
    }

    private String convertItemToText(final Object listItem) {
        if (listItem instanceof SelectItem) {
            return ((SelectItem) listItem).getLabel();
        }

        return listItem.toString();
    }
}
