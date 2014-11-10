package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlSecret;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlSecret.COMPONENT_FAMILY, rendererType = HtmlSecret.RENDERER_TYPE)
public class SecretRenderer extends HtmlBasicInputRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.INPUTSECRET);

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(htmlComponent, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        // render tooltip elements if necessary
        new TooltipPartRenderer().renderTooltip(htmlComponent, writer);

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
        writer.writeAttribute("type", "password", "type");
        writer.writeAttribute("name", component.getClientId(context),
                "clientId");

        final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                Constants.BOOTSTRAP_FORM_CONTROL,
                !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

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
        this.renderHtmlFeatures(component, writer);
        // *** END HTML 5 CHANGED ****************************

        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                ATTRIBUTES,
                getNonOnChangeBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderOnchange(context, component, false);

        writer.endElement("input");

    }

    protected void renderHtmlFeatures(UIComponent component, ResponseWriter writer) throws IOException {
        if (component instanceof HtmlSecret) {
            final HtmlSecret inputComponent = (HtmlSecret) component;
            new HtmlAttributePartRenderer().writePlaceholderAttribute(writer, inputComponent.getPlaceholder());
        }
    }
}
