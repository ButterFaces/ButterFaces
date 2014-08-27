package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlSecret;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
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
@FacesRenderer(componentFamily = HtmlSecret.COMPONENT_FAMILY, rendererType = HtmlSecret.RENDERER_TYPE)
public class SecretRenderer extends com.sun.faces.renderkit.html_basic.SecretRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.INPUTSECRET);

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

        // Render tooltip if components tooltip attribute is set
        new TooltipPartRenderer().renderTooltip(htmlComponent, true, writer, context);

        // Close inner component wrapper div
        new InnterComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    /**
     * Method copied from super class to add html features.
     */
    @Override
    protected void getEndTextToRender(final FacesContext context, final UIComponent component, String currentValue)
            throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String redisplay = String.valueOf(component.getAttributes().get("redisplay"));
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "password", "type");
        writer.writeAttribute("name", component.getClientId(context),
                "clientId");

        String autoComplete = (String)
                component.getAttributes().get("autocomplete");
        if (autoComplete != null) {
            // only output the autocomplete attribute if the value
            // is 'off' since its lack of presence will be interpreted
            // as 'on' by the browser
            if ("off".equals(autoComplete)) {
                writer.writeAttribute("autocomplete",
                        "off",
                        "autocomplete");
            }
        }

        // render default text specified
        if (currentValue != null) {
            writer.writeAttribute("value", currentValue, "value");
        }

        // *** BEGIN HTML 5 CHANGED **************************
        if (component instanceof HtmlSecret) {
            final HtmlSecret inputComponent = (HtmlSecret) component;
            if (inputComponent.getPlaceholder() != null && !"".equals(inputComponent.getPlaceholder())) {
                writer.writeAttribute("placeholder", inputComponent.getPlaceholder(), "placeholder");
            }
        }
        // *** END HTML 5 CHANGED ****************************

        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                ATTRIBUTES,
                getNonOnChangeBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderOnchange(context, component, false);

        String styleClass;
        if (null != (styleClass = (String)
                component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        writer.endElement("input");

    }
}
