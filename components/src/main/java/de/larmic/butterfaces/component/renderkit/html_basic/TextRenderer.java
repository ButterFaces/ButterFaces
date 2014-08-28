package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlText;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * larmic butterfaces components - An jsf 2 component extension https://bitbucket.org/larmicBB/larmic-butterfaces-components
 * <p/>
 * Copyright 2013 by Lars Michaelis <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */
@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlText.RENDERER_TYPE)
public class TextRenderer extends com.sun.faces.renderkit.html_basic.TextRenderer {

    private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);
    private static final Attribute[] OUTPUT_ATTRIBUTES = AttributeManager
            .getAttributes(AttributeManager.Key.OUTPUTTEXT);

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(htmlComponent, writer);

        // Open inner component wrapper div
        new InnterComponentWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnterComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
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

            // only output the autocomplete attribute if the value
            // is 'off' since its lack of presence will be interpreted
            // as 'on' by the browser
            if ("off".equals(component.getAttributes().get("autocomplete"))) {
                writer.writeAttribute("autocomplete", "off", "autocomplete");
            }

            // render default text specified
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // *** BEGIN HTML 5 CHANGED **************************
            if (component instanceof HtmlText) {
                final HtmlText inputComponent = (HtmlText) component;

                writeHTML5AttributeIfNotEmpty(writer, "placeholder", inputComponent.getPlaceholder());
                writeHTML5AttributeIfNotEmpty(writer, "pattern", inputComponent.getPattern());
                writeHTML5AttributeIfNotEmpty(writer, "min", inputComponent.getMin());
                writeHTML5AttributeIfNotEmpty(writer, "max", inputComponent.getMax());
                writeHTML5AttributeIfNotEmpty(writer, "type", inputComponent.getType(), "text");

                if (inputComponent.getAutoFocus()) {
                    writer.writeAttribute("autofocus", "true", null);
                }

            } else {
                writer.writeAttribute("type", "text", null);
            }
            // *** END HTML 5 CHANGED ****************************

            // style is rendered as a passthur attribute
            RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES,
                    getNonOnChangeBehaviors(component));
            RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

            RenderKitUtils.renderOnchange(context, component, false);

            writer.endElement("input");

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
