package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlTextArea.COMPONENT_FAMILY, rendererType = HtmlTextArea.RENDERER_TYPE)
public class TextAreaRenderer extends HtmlBasicInputRenderer {

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXTAREA);

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
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer, getComponentNameStyleClass());

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
    }

    /**
     * @return additional component style class for outer div.
     */
    protected String getComponentNameStyleClass() {
        return "butter-component-textarea";
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
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(component, writer);

        this.renderTooltipIfNecessary(context, component);
        this.renderAdditionalScript(context, component);

        // Render textarea counter
        new MaxLengthPartRenderer().renderMaxLength(htmlComponent, writer);

        // Render textarea expandable script call
        this.renderExpandable(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    protected void renderExpandable(HtmlInputComponent htmlComponent, ResponseWriter writer) throws IOException {
        new ExpandablePartRenderer().renderExpandable(htmlComponent, writer);
    }

    protected void renderAdditionalScript(final FacesContext context, final UIComponent component) throws IOException {
        // implement me
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
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

        writer.startElement("textarea", component);
        this.writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("name", component.getClientId(context), "clientId");

        final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                Constants.BOOTSTRAP_FORM_CONTROL,
                !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        // *** BEGIN HTML 5 CHANGED **************************
        this.renderHtmlFeatures(component, writer);
        // *** END HTML 5 CHANGED ****************************

        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderOnchange(context, component, false);

        // render default text specified
        if (currentValue != null) {
            writer.writeText(currentValue, component, "value");
        }

        writer.endElement("textarea");

    }

    protected void renderHtmlFeatures(final UIComponent component, final ResponseWriter writer) throws IOException {
        new HtmlAttributePartRenderer().renderHtmlFeatures(component, writer);
    }
}
