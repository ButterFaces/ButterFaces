package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlNumber;
import de.larmic.butterfaces.component.html.text.HtmlMaskedText;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.AbstractTextRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@FacesRenderer(componentFamily = HtmlNumber.COMPONENT_FAMILY, rendererType = HtmlNumber.RENDERER_TYPE)
public class NumberRenderer extends AbstractTextRenderer<HtmlMaskedText> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-number");
    }

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlNumber numberComponent = (HtmlNumber) component;

        if (!numberComponent.isReadonly()) {
            Map<String, String> options = new HashMap<>();
            if (StringUtils.isNotEmpty(numberComponent.getMin())) {
                options.put("min", numberComponent.getMin());
            }
            if (StringUtils.isNotEmpty(numberComponent.getMax())) {
                options.put("max", numberComponent.getMax());
            }
            if (StringUtils.isNotEmpty(numberComponent.getStep())) {
                options.put("step", numberComponent.getStep());
            }
            if ( numberComponent.isDisabled()) {
                options.put("disabled", numberComponent.isDisabled() + "");
            }

            RenderUtils.renderJQueryPluginCall(
                    component.getClientId(),
                    "butterNumberSpinner(" + RenderUtils.createOptionsStringForJQueryPluginCall(options) + ")",
                    writer,
                    numberComponent
            );
        }
    }
}