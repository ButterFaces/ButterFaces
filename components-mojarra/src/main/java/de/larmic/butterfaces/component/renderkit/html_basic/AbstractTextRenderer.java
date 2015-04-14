package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlText;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public abstract class AbstractTextRenderer<T extends HtmlInputComponent> extends HtmlBasicInputRenderer {

    private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final T htmlComponent = (T) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(htmlComponent, writer);

        this.encodeBeginInnerWrapper(htmlComponent, writer);
        this.encodeReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final T htmlComponent = (T) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            this.encodeEndContent(context, component, htmlComponent, writer);
        }

        this.encodeInnerEnd(component, writer);

        this.encodeEndInnerWrapper(htmlComponent, writer);

        // render tooltip elements if necessary
        new TooltipPartRenderer().renderTooltip(htmlComponent, writer);

        this.encodeEnd(component, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    protected void encodeEndContent(FacesContext context, UIComponent component, HtmlInputComponent htmlComponent, ResponseWriter writer) throws IOException {
        final UIComponent inputGroupAddonLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_LEFT);
        final UIComponent inputGroupAddonRightFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_RIGHT);
        final UIComponent inputGroupBtnLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_LEFT);
        final UIComponent inputGroupBtnRightFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_RIGHT);
        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_ADDON) && inputGroupAddonLeftFacet != null) {
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-addon", null);
            inputGroupAddonLeftFacet.encodeAll(context);
            writer.endElement("span");
        }
        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_BTN) && inputGroupBtnLeftFacet != null) {
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-btn", null);
            inputGroupBtnLeftFacet.encodeAll(context);
            writer.endElement("span");
        }
        super.encodeEnd(context, component);
        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_ADDON) && inputGroupAddonRightFacet != null) {
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-addon", null);
            inputGroupAddonRightFacet.encodeAll(context);
            writer.endElement("span");
        }
        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_BTN) && inputGroupBtnRightFacet != null) {
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-btn", null);
            inputGroupBtnRightFacet.encodeAll(context);
            writer.endElement("span");
        }
    }

    /**
     * @return true if normal readonly render option should be used. False is custom readonly option is created by renderer implementation.
     */
    protected boolean encodeReadonly() {
        return true;
    }

    protected void encodeReadonly(T htmlComponent, ResponseWriter writer) throws IOException {
        if (encodeReadonly()) {
            // Render readonly span if components readonly attribute is set
            new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
        }
    }

    protected void encodeEndInnerWrapper(T htmlComponent, ResponseWriter writer) throws IOException {
        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);
    }

    protected void encodeBeginInnerWrapper(T htmlComponent, ResponseWriter writer) throws IOException {
        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);
    }

    protected void encodeInnerEnd(final UIComponent component, final ResponseWriter writer) throws IOException {
        // override me
    }

    protected void encodeEnd(final UIComponent component, final ResponseWriter writer) throws IOException {
        // override me
    }

    /**
     * Helper to call super end from sub classes without calling encodeEnd of this class. This is not nice but is works at this time.
     * TODO fix it.
     */
    protected void encodeSuperEnd(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeEnd(context, component);
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

            final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                    Constants.BOOTSTRAP_FORM_CONTROL,
                    !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

            if (StringUtils.isNotEmpty(styleClass)) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // *** BEGIN HTML 5 CHANGED **************************
            this.renderHtmlFeatures(component, writer);
            // *** END HTML 5 CHANGED ****************************

            // style is rendered as a passthur attribute
            RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES,
                    getNonOnChangeBehaviors(component));
            RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

            RenderKitUtils.renderOnchange(context, component, false);

            writer.endElement("input");
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
        new HtmlAttributePartRenderer().renderHtmlFeatures(component, writer);

        if (component instanceof HtmlText) {
            final HtmlText inputComponent = (HtmlText) component;

            final HtmlAttributePartRenderer htmlAttributePartRenderer = new HtmlAttributePartRenderer();
            htmlAttributePartRenderer.writePatternAttribute(writer, inputComponent.getPattern());
            htmlAttributePartRenderer.writeMinAttribute(writer, inputComponent.getMin());
            htmlAttributePartRenderer.writeMaxAttribute(writer, inputComponent.getMax());
            htmlAttributePartRenderer.writeTypeAttribute(writer, inputComponent.getType());
        }

        writer.writeAttribute("type", "text", null);
    }


}
