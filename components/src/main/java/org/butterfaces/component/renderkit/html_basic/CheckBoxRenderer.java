/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic;

import org.butterfaces.component.html.HtmlCheckBox;
import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.partrenderer.CheckBoxReadonlyPartRenderer;
import org.butterfaces.component.partrenderer.Constants;
import org.butterfaces.component.partrenderer.InnerComponentCheckBoxWrapperPartRenderer;
import org.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;
import org.butterfaces.util.StringJoiner;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlCheckBox.COMPONENT_FAMILY, rendererType = HtmlCheckBox.RENDERER_TYPE)
public class CheckBoxRenderer extends AbstractHtmlTagRenderer<HtmlCheckBox> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        final HtmlCheckBox checkBox = (HtmlCheckBox) component;
        final String styleClass = checkBox.isSwitch() ? "butter-component-checkbox switch" : "butter-component-checkbox";
        encodeBegin(context, component, styleClass);
    }

    @Override
    protected void encodeEndInnerWrapper(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlCheckBox checkBox = (HtmlCheckBox) component;

        if (checkBox.isSwitch() && !checkBox.isReadonly()) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "slider round", "styleClass");
            writer.endElement("div");
        }

        new InnerComponentCheckBoxWrapperPartRenderer().renderInnerWrapperEnd(checkBox, writer);
    }

    @Override
    protected void encodeEnd(final HtmlCheckBox checkBox, final ResponseWriter writer) throws IOException {
        if (checkBox.isSwitch() && !checkBox.isReadonly()) {
            writer.startElement("script", checkBox);
            writer.writeText("jQuery(function () {\n", null);
            writer.writeText("ButterFaces.Checkbox.addSwitchClickEvent('" + checkBox.getClientId() + "');\n", null);
            writer.writeText("});", null);
            writer.endElement("script");
        }
    }

    @Override
    protected void encodeBeginInnerWrapper(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlCheckBox checkBox = (HtmlCheckBox) component;
        new InnerComponentCheckBoxWrapperPartRenderer().renderInnerWrapperBegin(checkBox, writer);
    }

    protected void renderInputStyleClass(final HtmlInputComponent component,
                                         final ResponseWriter writer) throws IOException {
        final String validationMarkerClass = !component.isValid() ? Constants.INVALID_STYLE_CLASS : null;
        final String styleClass = StringJoiner.on(" ")
                .join(Constants.INPUT_COMPONENT_MARKER)
                .join("mt-1 mr-2")
                .join(validationMarkerClass)
                .toString();
        writer.writeAttribute("class", styleClass, "styleClass");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (!(component instanceof HtmlCheckBox)) {
            return;
        }

        final HtmlCheckBox checkBox = (HtmlCheckBox) component;

        if (!component.isRendered() || checkBox.isReadonly()) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        final Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        setSubmittedValue(component, isChecked(requestMap.get(clientId)));
    }

    @Override
    public Object getConvertedValue(final FacesContext context,
                                    final UIComponent component,
                                    final Object submittedValue) throws ConverterException {
        return ((submittedValue instanceof Boolean) ? submittedValue : Boolean.valueOf(submittedValue.toString()));
    }

    @Override
    protected void encodeTagType(UIComponent component, ResponseWriter writer) throws IOException {
        writer.writeAttribute("type", "checkbox", "type");
    }

    @Override
    protected void encodeReadonly(HtmlCheckBox htmlComponent, ResponseWriter writer) throws IOException {
        if (encodeReadonly()) {
            // Render readonly span if components readonly attribute is set
            new CheckBoxReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
        }
    }

    @Override
    protected void encodeAdditionalTagAttributes(UIComponent component, ResponseWriter writer, String currentValue) throws IOException {
        if (Boolean.valueOf(currentValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }
    }

    private static boolean isChecked(String value) {

        return "on".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "true".equalsIgnoreCase(value);

    }
}
