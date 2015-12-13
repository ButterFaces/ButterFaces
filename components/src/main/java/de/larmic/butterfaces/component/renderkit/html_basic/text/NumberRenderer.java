package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlNumber;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@FacesRenderer(componentFamily = HtmlNumber.COMPONENT_FAMILY, rendererType = HtmlNumber.RENDERER_TYPE)
public class NumberRenderer extends AbstractHtmlTagRenderer<HtmlNumber> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-number");
    }

    @Override
    protected void encodeEnd(HtmlNumber numberComponent, ResponseWriter writer) throws IOException {
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
                    numberComponent.getClientId(),
                    "butterNumberSpinner(" + RenderUtils.createOptionsStringForJQueryPluginCall(options) + ")",
                    writer,
                    numberComponent
            );
        }
    }
}