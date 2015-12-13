package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.html.text.part.HtmlAutoComplete;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlText.RENDERER_TYPE)
public class TextRenderer extends AbstractHtmlTagRenderer<HtmlText> {

    @Override
    protected void encodeEnd(final HtmlText component, final ResponseWriter writer) throws IOException {
        final HtmlAutoComplete autoCompleteChild = findAutoCompleteChild(component);
        if (autoCompleteChild != null) {
            RenderUtils.renderJQueryPluginCall(autoCompleteChild.getClientId(), "_butterAutoCompleteInit()", writer, component);
        }
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        super.decode(context, component);

        final HtmlText text = (HtmlText) component;

        final HtmlAutoComplete autoCompleteChild = findAutoCompleteChild(text);

        if (autoCompleteChild != null) {
            final ExternalContext external = context.getExternalContext();
            final Map<String, String> params = external.getRequestParameterMap();
            final String behaviorEvent = params.get("javax.faces.behavior.event");
            final String searchValue = params.get("params");

            if ("autocomplete".equals(behaviorEvent) && StringUtils.isNotEmpty(searchValue)) {
                autoCompleteChild.getCachedAutoCompleteValues().clear();
                text.setSubmittedValue(null);
                text.setLocalValueSet(false);
                final List<String> values = autoCompleteChild.getAutoComplete().autoComplete(searchValue);
                if (values != null) {
                    autoCompleteChild.getCachedAutoCompleteValues().addAll(values);
                }
            }
        }
    }

    @Override
    protected void postEncodeInput(FacesContext context, UIComponent component) throws IOException {
        final HtmlAutoComplete autoCompleteChild = findAutoCompleteChild(component);
        if (autoCompleteChild != null) {
            autoCompleteChild.encodeAll(context);
        }
    }

    @Override
    protected void renderAdditionalInputAttributes(final FacesContext context,
                                                   final UIComponent component,
                                                   final ResponseWriter writer) throws IOException {
        final HtmlAutoComplete autoCompleteChild = findAutoCompleteChild(component);
        if (autoCompleteChild != null) {
            writer.writeAttribute("autocomplete", "off", null);
            writer.writeAttribute("autocorrect", "off", null);
        }
    }

    private HtmlAutoComplete findAutoCompleteChild(final UIComponent component) {
        HtmlAutoComplete autoComplete = null;

        for (UIComponent uiComponent : component.getChildren()) {
            if (uiComponent instanceof HtmlAutoComplete) {
                autoComplete = (HtmlAutoComplete) uiComponent;
            }
        }

        return autoComplete;
    }
}
