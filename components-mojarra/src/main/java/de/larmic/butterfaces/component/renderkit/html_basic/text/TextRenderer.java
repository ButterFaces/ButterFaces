package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.html.text.part.HtmlAutoComplete;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlText.RENDERER_TYPE)
public class TextRenderer extends AbstractTextRenderer<HtmlText> {
    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        super.decode(context, component);

        final HtmlText text = (HtmlText) component;

        final HtmlAutoComplete autoCompleteChild = findAutoCompleteChild(text);

        if (autoCompleteChild != null) {
            final ExternalContext external = context.getExternalContext();
            final Map<String, String> params = external.getRequestParameterMap();
            final String behaviorEvent = params.get("javax.faces.behavior.event");

            if ("autocomplete".equals(behaviorEvent)) {
                autoCompleteChild.getCachedAutoCompleteValues().clear();
                final List<String> values = autoCompleteChild.getAutoComplete().autoComplete(text.getSubmittedValue());
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
    protected void renderPassThruAttributes(final FacesContext context,
                                            final UIComponent component,
                                            final ResponseWriter writer) throws IOException {
        super.renderPassThruAttributes(context, component, writer);

        final HtmlText text = (HtmlText) component;

        if (findAutoCompleteChild(text) != null) {
            final String suggestFunction = "jsf.ajax.request(this,'autocomplete',{'javax.faces.behavior.event':'autocomplete',render:'" + findAutoCompleteChild(text).getClientId() + "'});";
            writer.writeAttribute("onkeyup", suggestFunction, null);
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
