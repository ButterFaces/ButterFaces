package de.larmic.butterfaces.component.renderkit.html_basic.action;

import de.larmic.butterfaces.component.behavior.JsfAjaxRequest;
import de.larmic.butterfaces.component.html.action.HtmlCommandLink;
import de.larmic.butterfaces.resolver.AjaxClientIdResolver;
import de.larmic.butterfaces.resolver.WebXmlParameters;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.*;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Map;

/**
 * Created by larmic on 16.09.14.
 */
@FacesRenderer(componentFamily = HtmlCommandLink.COMPONENT_FAMILY, rendererType = HtmlCommandLink.RENDERER_TYPE)
public class CommandLinkRenderer extends com.sun.faces.renderkit.html_basic.CommandLinkRenderer {

    private WebXmlParameters webXmlParameters;

    /**
     * Will be set in renderAsActive if f:ajax child with onevent attribute exists.
     */
    private String onEventCallback = null;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        this.onEventCallback = null;

        final HtmlCommandLink link = (HtmlCommandLink) component;
        final ResponseWriter writer = context.getResponseWriter();

        webXmlParameters = new WebXmlParameters(context.getExternalContext());

        if (!link.isDisabled()) {
            this.renderAsActive(context, component);
        } else {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            writeCommonLinkAttributes(writer, component);
            if (StringUtils.isNotEmpty(link.getStyle())) {
                writer.writeAttribute("style", link.getStyle(), "style");
            }
            this.writeValue(component, writer);
        }
    }

    @Override
    protected boolean shouldWriteIdAttribute(UIComponent component) {
        return true;
    }

    @Override
    protected void writeCommonLinkAttributes(final ResponseWriter writer, final UIComponent component) throws IOException {
        final HtmlCommandLink link = (HtmlCommandLink) component;
        final String styleClass = (String) component.getAttributes().get("styleClass");

        final StringBuilder generatedStyleClass = new StringBuilder(StringUtils.isEmpty(styleClass) ? "" : styleClass);

        if (link.isDisabled()) {
            generatedStyleClass.append(" btn-disabled");
        }
        if (StringUtils.isEmpty(link.getGlyphicon())) {
            generatedStyleClass.append(" no-glyphicon");
        }

        if (generatedStyleClass.length() > 0) {
            writer.writeAttribute("class", generatedStyleClass.toString(), "styleClass");
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlCommandLink link = (HtmlCommandLink) component;

        if (link.isDisabled()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String resetValues = params.get("javax.faces.partial.resetValues");
        final String render = params.get("javax.faces.partial.render");

        if (StringUtils.isNotEmpty(resetValues) && StringUtils.isNotEmpty(render) && Boolean.valueOf(resetValues)) {
            final String[] split = render.split(" ");

            for (String clientId : split) {
                final UIComponent renderComponent = context.getViewRoot().findComponent(clientId);
                resetValues(renderComponent);
            }
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        if (params.containsKey(clientId) || isPartialOrBehaviorAction(clientId, params)) {
            component.queueEvent(new ActionEvent(component));
        }
    }

    private boolean isPartialOrBehaviorAction(String clientId, Map<String, String> requestParameter) {
        if (clientId == null || clientId.length() == 0) {
            return false;
        }

        if (!clientId.equals(requestParameter.get("javax.faces.source"))) {
            return false;
        }

        // check for a behavior action event
        final String behaviorEvent = requestParameter.get("javax.faces.behavior.event");
        if (behaviorEvent != null) {
            return "action".equals(behaviorEvent);
        }

        // Not a Behavior-related request. Check for jsf.ajax.request() request params
        return ("click".equals(requestParameter.get("javax.faces.partial.event")));
    }

    private void resetValues(final UIComponent component) {
        if (component == null) {
            return;
        }

        for (UIComponent child : component.getChildren()) {
            resetValues(child);
        }

        if (component instanceof UIOutput) {
            ((UIOutput) component).resetValue();
        } else if (component instanceof EditableValueHolder) {
            ((EditableValueHolder) component).resetValue();
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final HtmlCommandLink link = (HtmlCommandLink) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!link.isDisabled()) {
            if (link.isAjaxDisableLinkOnRequest()) {
                writer.startElement("script", component);
                writer.writeText("function " + getOnEventListenerName(component) + "(data) {", null);
                if (StringUtils.isNotEmpty(onEventCallback)) {
                    writer.writeText("    " + onEventCallback + "(data);", null);
                }

                final String processingText = createAjaxProcessingText(link);
                final String processingGlyphicon = createAjaxProcessingGlypicon(link);

                final AjaxClientIdResolver ajaxClientIdResolver = new AjaxClientIdResolver(link);
                final String jQueryIDSelector = isAjaxDisableRenderReqionOnRequest(link, webXmlParameters)
                        ? ajaxClientIdResolver.getjQueryRenderIDSelector() : "undefined";

                writer.writeText(
                        createDisableOnClickFunctionCall(link, processingText, processingGlyphicon, jQueryIDSelector),
                        null
                );

                writer.writeText("}", null);
                writer.endElement("script");
            }
            writer.endElement("a");
        } else {
            writer.endElement("span");
        }
    }

    private Boolean isAjaxDisableRenderReqionOnRequest(final HtmlCommandLink link, final WebXmlParameters parameters) {
        final Boolean disableRegion = link.isAjaxDisableRenderRegionsOnRequest();
        return disableRegion != null ? disableRegion : parameters.isAjaxDisableRenderRegionsOnRequest();
    }

    private String createDisableOnClickFunctionCall(HtmlCommandLink link, String processingText,
                                                    String processingGlyphicon, String jQueryIDSelector) {
        final StringBuilder sb = new StringBuilder();
        sb.append("    butter.link.disableOnClick(data, ");
        sb.append(link.isAjaxShowWaitingDotsOnRequest()).append(",");

        if (link.getValue() != null) {
            sb.append("'").append(link.getValue()).append("'");
        } else {
            sb.append("null");
        }
        sb.append(",'");

        sb.append(processingText).append("','");
        sb.append(link.getGlyphicon()).append("','");
        sb.append(processingGlyphicon).append("',");
        sb.append(link.isAjaxHideGlyphiconOnRequest()).append(",'");
        sb.append(jQueryIDSelector).append("');");

        return sb.toString();
    }

    private String createAjaxProcessingText(final HtmlCommandLink link) {
        if (StringUtils.isNotEmpty(link.getAjaxProcessingTextOnRequest())) {
            return link.getAjaxProcessingTextOnRequest();
        }

        return webXmlParameters.getAjaxProcessingTextOnRequest();
    }

    private String createAjaxProcessingGlypicon(final HtmlCommandLink link) {
        if (StringUtils.isNotEmpty(link.getAjaxProcessingGlyphiconOnRequest())) {
            return link.getAjaxProcessingGlyphiconOnRequest();
        }

        return webXmlParameters.getAjaxProcessingGlyphiconOnRequest();
    }

    @Override
    protected void writeValue(final UIComponent component, final ResponseWriter writer) throws IOException {
        final HtmlCommandLink commandLink = (HtmlCommandLink) component;

        this.writeGlyphiconIfNecessary(commandLink, writer);

        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-glyphicon-text", null);

        final Object value = ((UICommand) component).getValue();
        if (value != null) {
            final String label = value.toString();

            if (StringUtils.isNotEmpty(label)) {
                writer.writeText(label, component, null);
            }
        }

        writer.endElement("span");

        this.writeWaitingDotsIfNecessary(commandLink, writer);
    }

    @Override
    protected void renderAsActive(FacesContext context, UIComponent component) throws IOException {
        AjaxBehavior ajaxBehavior = null;
        final HtmlCommandLink link = (HtmlCommandLink) component;

        if (link.isAjaxDisableLinkOnRequest()) {
            // TODO should this be working for multiple behaviors?
            ajaxBehavior = JsfAjaxRequest.findFirstActiveAjaxBehavior(link, "action");

            if(ajaxBehavior != null) {
                if (StringUtils.isNotEmpty(ajaxBehavior.getOnevent())) {
                    onEventCallback = ajaxBehavior.getOnevent();
                }

                ajaxBehavior.setOnevent(getOnEventListenerName(component));
                ajaxBehavior.setOnerror(getOnEventListenerName(component));
            }
        }

        // TODO replace it with custom implementation (replace mojarra.ab(,,,) by default jsf js api)
        super.renderAsActive(context, component);

        // reset ajax behaviour because otherwise a render of this component will not be work correctly (wrong js
        // callback is registered if onevent is set on f:ajax.
        if (ajaxBehavior != null) {
            ajaxBehavior.setOnevent(onEventCallback);
        }
    }

    private String getOnEventListenerName(final UIComponent component) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        return "glyphiconLinkListener" + "_" + component.getClientId().replace(separatorChar + "", "_");
    }

    protected void writeWaitingDotsIfNecessary(final HtmlCommandLink commandLink,
                                               final ResponseWriter writer) throws IOException {
        if (commandLink.isAjaxDisableLinkOnRequest()) {
            writer.startElement("span", commandLink);
            writer.writeAttribute("class", "butter-component-glyphicon-processing", null);
            writer.endElement("span");
        }
    }

    protected void writeGlyphiconIfNecessary(final HtmlCommandLink commandLink,
                                             final ResponseWriter writer) throws IOException {
        final String glyphicon = StringUtils.getNotNullValue(commandLink.getGlyphicon(), "");

        writer.startElement("span", commandLink);
        writer.writeAttribute("class", "butter-component-glyphicon " + glyphicon, null);
        writer.endElement("span");
    }
}
