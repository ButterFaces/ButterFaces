package de.larmic.jsf2.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.jsf2.component.html.HtmlInputComponent;
import de.larmic.jsf2.component.html.HtmlNumber;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * larmic jsf2 components - An jsf 2 component extension https://bitbucket.org/larmicBB/larmic-jsf2-components
 * <p/>
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlNumber.COMPONENT_FAMILY, rendererType = HtmlNumber.RENDERER_TYPE)
public class NumberRenderer extends com.sun.faces.renderkit.html_basic.TextRenderer {

    private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);
    private static final Attribute[] OUTPUT_ATTRIBUTES = AttributeManager
            .getAttributes(AttributeManager.Key.OUTPUTTEXT);

    private final InputRendererSupport inputRendererSupport = new InputRendererSupport();

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        this.inputRendererSupport.encodeBegin(context, (HtmlInputComponent) component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        this.inputRendererSupport.encodeEnd(context, htmlComponent);
    }

    /**
     * Method copied from super class to add html features.
     */
    @Override
    protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
            throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        boolean shouldWriteIdAttribute = false;
        boolean isOutput = false;

        final String style = (String) component.getAttributes().get("style");
        final String styleClass = (String) component.getAttributes().get("styleClass");
        final String dir = (String) component.getAttributes().get("dir");
        final String lang = (String) component.getAttributes().get("lang");
        final String title = (String) component.getAttributes().get("title");
        if (component instanceof UIInput) {
            writer.startElement("input", component);
            this.writeIdAttributeIfNecessary(context, writer, component);
            writer.writeAttribute("name", (component.getClientId(context)), "clientId");
            writer.writeAttribute("type", "text", null);

            // render default text specified
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // *** BEGIN CUSTOM CHANGED **************************
            writer.writeAttribute("autocomplete", "off", "autocomplete");
            // *** END CUSTOM CHANGED ****************************

            // *** BEGIN HTML 5 CHANGED **************************
            if (component instanceof HtmlNumber) {
                final HtmlNumber inputComponent = (HtmlNumber) component;

                writeHTML5AttributeIfNotEmpty(writer, "placeholder", inputComponent.getPlaceholder());

                if (inputComponent.getAutoFocus()) {
                    writer.writeAttribute("autofocus", "true", null);
                }
            }
            // *** END HTML 5 CHANGED ****************************

            // style is rendered as a passthur attribute
            RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES,
                    getNonOnChangeBehaviors(component));
            RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

            RenderKitUtils.renderOnchange(context, component, false);

            writer.endElement("input");

            // *** BEGIN CUSTOM CHANGED **************************
            if (component instanceof HtmlNumber) {
                final HtmlNumber inputComponent = (HtmlNumber) component;

                writer.startElement("script", component);
                writer.writeText("window.onload = function() {", null);
                writer.writeText("var _component = document.getElementById('" + component.getClientId() + "');", null);
                writer.writeText("_component.addEventListener('keyup', function(e) {handleSpinner(e, _component, "
                        + ("".equals(inputComponent.getMin()) ? "null" : "'" + inputComponent.getMin() + "'") + ", "
                        + ("".equals(inputComponent.getMax()) ? "null" : "'" + inputComponent.getMax() + "'") + ")}, false);", null);
                writer.writeText("}", null);
                writer.endElement("script");
            }
            // *** END CUSTOM CHANGED ****************************

        } else if (isOutput = (component instanceof UIOutput)) {
            if (styleClass != null || style != null || dir != null || lang != null || title != null
                    || (shouldWriteIdAttribute = this.shouldWriteIdAttribute(component))) {
                writer.startElement("span", component);
                this.writeIdAttributeIfNecessary(context, writer, component);
                if (null != styleClass) {
                    writer.writeAttribute("class", styleClass, "styleClass");
                }
                // style is rendered as a passthru attribute
                RenderKitUtils.renderPassThruAttributes(context, writer, component, OUTPUT_ATTRIBUTES);

            }
            if (currentValue != null) {
                final Object val = component.getAttributes().get("escape");
                if ((val != null) && Boolean.valueOf(val.toString())) {
                    writer.writeText(currentValue, component, "value");
                } else {
                    writer.write(currentValue);
                }
            }
        }
        if (isOutput
                && (styleClass != null || style != null || dir != null || lang != null || title != null || (shouldWriteIdAttribute))) {
            writer.endElement("span");
        }

    }

    private void writeHTML5AttributeIfNotEmpty(ResponseWriter writer, String attributeName, String attributeValue) throws IOException {
        writeHTML5AttributeIfNotEmpty(writer, attributeName, attributeValue, null);
    }

    private void writeHTML5AttributeIfNotEmpty(ResponseWriter writer, String attributeName, String attributeValue, String alternativeValue) throws IOException {
        if (attributeValue != null && !"".equals(attributeValue)) {
            writer.writeAttribute(attributeName, attributeValue, attributeName);
        } else if (alternativeValue != null && !"".equals(alternativeValue)) {
            writer.writeAttribute(attributeName, alternativeValue, attributeName);
        }
    }
}
