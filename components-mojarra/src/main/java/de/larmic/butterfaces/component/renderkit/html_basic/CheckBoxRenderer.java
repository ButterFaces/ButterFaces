package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlCheckBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Wrapped a copy of Mojarras {@link de.larmic.butterfaces.component.renderkit.html_basic.CheckBoxRenderer}.
 */
@FacesRenderer(componentFamily = HtmlCheckBox.COMPONENT_FAMILY, rendererType = HtmlCheckBox.RENDERER_TYPE)
public class CheckBoxRenderer extends HtmlBasicInputRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.SELECTBOOLEANCHECKBOX);

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        super.encodeBegin(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer);

        // Open inner component wrapper div
        new InnerComponentCheckBoxWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentCheckBoxWrapperPartRenderer().renderInnerWrapperEnd(htmlComponent, writer);

        renderTooltipIfNecessary(context, component);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        new TooltipPartRenderer().renderTooltipIfNecessary(context, component);
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }
        assert (clientId != null);
        // Convert the new value

        Map<String, String> requestParameterMap = context.getExternalContext()
                .getRequestParameterMap();
        boolean isChecked = isChecked(requestParameterMap.get(clientId));
        setSubmittedValue(component, isChecked);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                    "new value after decoding: {0}",
                    isChecked);
        }

    }

    @Override
    public Object getConvertedValue(final FacesContext context,
                                    final UIComponent component,
                                    final Object submittedValue) throws ConverterException {
        return ((submittedValue instanceof Boolean) ? submittedValue : Boolean.valueOf(submittedValue.toString()));
    }

    @Override
    protected void getEndTextToRender(final FacesContext context,
                                      final UIComponent component,
                                      final String currentValue) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "checkbox", "type");
        writer.writeAttribute("name", component.getClientId(context), "name");

        if (Boolean.valueOf(currentValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }

        final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        // *** BEGIN HTML 5 CHANGED **************************
        this.renderHtmlFeatures(component, writer);
        // *** END HTML 5 CHANGED ****************************

        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES, getNonOnClickSelectBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderSelectOnclick(context, component, false);

        writer.endElement("input");

    }

    protected void renderHtmlFeatures(UIComponent component, ResponseWriter writer) throws IOException {
        new HtmlAttributePartRenderer().renderHtmlFeatures(component, writer);
    }

    /**
     * @param value the submitted value
     * @return "true" if the component was checked, otherise "false"
     */
    private static boolean isChecked(String value) {

        return "on".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "true".equalsIgnoreCase(value);

    }
}
