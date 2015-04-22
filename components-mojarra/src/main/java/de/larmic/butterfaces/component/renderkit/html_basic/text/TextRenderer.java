package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.html.text.part.HtmlAutoComplete;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
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
    protected void encodeEnd(final UIComponent component, final ResponseWriter writer) throws IOException  {
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
        final HtmlText text = (HtmlText) component;

        if (findAutoCompleteChild(text) != null) {
            final String suggestFunction = createAjaxRequest(text);
            writer.writeAttribute("onkeyup", suggestFunction, null);
        }

        super.renderPassThruAttributes(context, component, writer);
    }

    private String createAjaxRequest(final HtmlText text) {
        final List<ClientBehavior> keyup = text.getClientBehaviors().get("keyup");

        final String autoCompleteClientId = findAutoCompleteChild(text).getClientId();
        String render = autoCompleteClientId;

        if (keyup != null && !keyup.isEmpty()) {
            final AjaxBehavior ajaxBehavior = (AjaxBehavior) keyup.get(0);
            if (!ajaxBehavior.isDisabled()) {
                for (String keyupRender : ajaxBehavior.getRender()) {
                    render += " " + keyupRender;
                }
            }
        }

        final String onevent = "function(data) { if (data.status === 'success') {jQuery(document.getElementById('" + autoCompleteClientId + "'))._butterAutoCompleteOnKeyUp()}}";
        return "jsf.ajax.request(this,'autocomplete',{'javax.faces.behavior.event':'autocomplete',render:'" + render + "',onevent:" + onevent + "});";
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
