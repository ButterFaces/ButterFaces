package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlNumber;
import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlNumber.COMPONENT_FAMILY, rendererType = HtmlNumber.RENDERER_TYPE)
public class NumberRenderer extends HtmlBasicInputRenderer {

    private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);
    private static final Attribute[] OUTPUT_ATTRIBUTES = AttributeManager
            .getAttributes(AttributeManager.Key.OUTPUTTEXT);

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

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlNumber htmlComponent = (HtmlNumber) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        // render tooltip elements if necessary
        renderTooltipIfNecessary(context, htmlComponent);

        final String min = "".equals(htmlComponent.getMin()) ? "0" : htmlComponent.getMin();
        final String max = "".equals(htmlComponent.getMax()) ? "100" : htmlComponent.getMax();

        writer.startElement("script", component);
        //writer.writeText("jQuery(document.getElementById('" + component.getClientId() + "')).find('input.butter-number-component').TouchSpin({ verticalbuttons: true, min:" + min + ", max:" + max + " });", null);
        writer.writeText("jQuery(document.getElementById('" + component.getClientId() + "')).find('input.butter-number-component').TouchSpin({ verticalbuttons: true });", null);
        writer.endElement("script");

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        for (UIComponent uiComponent : component.getChildren()) {
            if (uiComponent instanceof HtmlTooltip) {
                uiComponent.encodeAll(context);
                if (uiComponent.isRendered()) {
                    break;
                }
            }
        }

        new TooltipPartRenderer().renderTooltipIfNecessary(context, component);
    }

    /**
     * Method copied from super class to add html features.
     */
    @Override
    protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
            throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (component instanceof UIInput) {
            writer.startElement("input", component);
            this.writeIdAttributeIfNecessary(context, writer, component);
            writer.writeAttribute("name", (component.getClientId(context)), "clientId");
            writer.writeAttribute("type", "text", null);

            final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                    Constants.BOOTSTRAP_FORM_CONTROL, "butter-number-component",
                    !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

            if (StringUtils.isNotEmpty(styleClass)) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // render default text specified
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }

            // *** BEGIN CUSTOM CHANGED **************************
            writer.writeAttribute("autocomplete", "off", "autocomplete");
            // *** END CUSTOM CHANGED ****************************

            // *** BEGIN HTML 5 CHANGED **************************
            this.renderHtmlFeatures(component, writer);
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
                writer.writeText("var _component = document.getElementById('" + component.getClientId() + "');", null);
                writer.writeText("_component.addEventListener('keyup', function(e) {butter.number.handleSpinner(e, _component, "
                        + convertMinMax(inputComponent.getMin()) + ", "
                        + convertMinMax(inputComponent.getMax()) + ")}, false);", null);
                writer.endElement("script");
            }
            // *** END CUSTOM CHANGED ****************************

        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

        boolean renderChildren = WebConfiguration.getInstance()
                .isOptionEnabled(WebConfiguration.BooleanWebContextInitParameter.AllowTextChildren);

        if (!renderChildren) {
            return;
        }

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                encodeRecursive(context, kid);
            }
        }

    }

    protected void renderHtmlFeatures(UIComponent component, ResponseWriter writer) throws IOException {
        if (component instanceof HtmlNumber) {
            final HtmlNumber inputComponent = (HtmlNumber) component;
            new HtmlAttributePartRenderer().writePlaceholderAttribute(writer, inputComponent.getPlaceholder());

            if (inputComponent.getAutoFocus()) {
                writer.writeAttribute("autofocus", "true", null);
            }
        }
    }

    private String convertMinMax(String value) {
        try {
            return "'" + Integer.valueOf(value) + "'";
        } catch (NumberFormatException e) {
            return "null";
        }
    }
}
